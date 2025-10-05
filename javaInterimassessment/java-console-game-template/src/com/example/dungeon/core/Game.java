package com.example.dungeon.core;

import com.example.dungeon.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });

        // about — показывает лог из WorldInfo (класслоадер, стат. инициализацию, touch-следы)
        commands.put("about", (ctx, a) -> {
            System.out.println("DungeonMini — учебный проект (Java).");
            System.out.println(WorldInfo.getLog());
        });

        // look — уже было: печатаем описание текущей комнаты
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));

        // TODO-1 → РЕАЛИЗОВАНО: move <north|south|east|west>
        commands.put("move", (ctx, a) -> {
            if (a.isEmpty()) throw new InvalidCommandException("Использование: move <north|south|east|west>");
            String dir = a.get(0).toLowerCase(Locale.ROOT);
            Room cur = ctx.getCurrent();
            Room next = cur.getNeighbors().get(dir);
            if (next == null) throw new InvalidCommandException("Нет пути на " + dir);
            ctx.setCurrent(next);
            System.out.println("Вы перешли в: " + next.getName());
            // Показать контекст комнаты сразу после перемещения
            System.out.println(next.describe());
        });

        // TODO-2 → РЕАЛИЗОВАНО: take <item name>
        commands.put("take", (ctx, a) -> {
            if (a.isEmpty()) throw new InvalidCommandException("Использование: take <item name>");
            String name = String.join(" ", a);
            Room r = ctx.getCurrent();

            Optional<Item> item = r.getItems().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(name))
                    .findFirst();

            if (item.isEmpty()) throw new InvalidCommandException("Нет предмета: " + name);

            r.getItems().remove(item.get());
            ctx.getPlayer().getInventory().add(item.get());
            System.out.println("Взято: " + item.get().getName());
        });

        // TODO-3 → РЕАЛИЗОВАНО: inventory (Stream API — группировка + сортировка)
        commands.put("inventory", (ctx, a) -> {
            var inv = ctx.getPlayer().getInventory();
            if (inv.isEmpty()) {
                System.out.println("(пусто)");
                return;
            }
            Map<String, List<Item>> byType = inv.stream()
                    .collect(Collectors.groupingBy(i -> i.getClass().getSimpleName()));

            byType.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> {
                        String items = e.getValue().stream()
                                .sorted(Comparator.comparing(Item::getName))
                                .map(Item::getName)
                                .collect(Collectors.joining(", "));
                        System.out.println("- " + e.getKey() + " (" + e.getValue().size() + "): " + items);
                    });
        });

        // TODO-4 → РЕАЛИЗОВАНО: use <item name> (полиморфизм через Item.apply())
        commands.put("use", (ctx, a) -> {
            if (a.isEmpty()) throw new InvalidCommandException("Использование: use <item name>");
            String name = String.join(" ", a);
            var inv = ctx.getPlayer().getInventory();
            Item item = inv.stream()
                    .filter(i -> i.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("В инвентаре нет: " + name));
            item.apply(ctx); // конкретное действие определяет сам предмет
        });

        // TODO-5 → РЕАЛИЗОВАНО: fight (простой пошаговый раунд боя)
        commands.put("fight", (ctx, a) -> {
            Room r = ctx.getCurrent();
            Monster m = r.getMonster();
            if (m == null || !m.isAlive()) {
                throw new InvalidCommandException("Здесь некому противостоять.");
            }
            Player p = ctx.getPlayer();

            // Ход игрока
            int dmg = Math.max(1, p.getAttack());
            m.setHp(m.getHp() - dmg);
            System.out.println("Вы бьёте " + m.getName() + " на " + dmg + ". HP монстра: " + m.getHp());

            if (!m.isAlive()) {
                System.out.println("Монстр повержен!");
                // Лут как награда (в духе примера)
                r.getItems().add(new Potion("Зелье из монстра", 3));
                ctx.addScore(10);
                return;
            }

            // Ответный удар монстра
            // В шаблоне у монстра нет явного attack в конструкторе, поэтому делаем базовый урон = 1 (как в примере ТЗ)
            int mdmg = 1;
            p.setHp(p.getHp() - mdmg);
            System.out.println("Монстр отвечает на " + mdmg + ". Ваше HP: " + p.getHp());

            if (!p.isAlive()) {
                System.out.println("Вы умерли. Игра окончена. Итоговые очки: " + ctx.getScore());
                System.exit(0);
            }
        });

        // сохранение/загрузка/таблица очков (I/O + try-with-resources в SaveLoad)
        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());

        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });

        // Примеры различий ошибок (оставлены закомментированными):
        // Компиляционная ошибка (если раскомментировать — проект не соберётся):
        // int x = "строка";
        // Ошибка выполнения (если раскомментировать — произойдёт во время запуска):
        // int z = 1 / 0; // ArithmeticException
    }

    private void bootstrapWorld() {
        Player hero = new Player("Герой", 20, 5);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);

        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 5, 288));
        cave.getItems().add(new Potion("Большое зелье", 10));
        cave.setMonster(new Monster("Скелет", 3, 150));
        

        state.setCurrent(square);
    }

    public void run() {
        System.out.println("DungeonMini. 'help' — список команд.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                // Важно: в Java строковый литерал должен быть "\\s+" (двойной слеш), чтобы это был regex пробелов
                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase(Locale.ROOT);
                List<String> args = parts.length > 1
                        ? Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length))
                        : List.of();

                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
