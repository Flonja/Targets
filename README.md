# Targets
Use selectors in the Nukkit commands

Use @a, @e, @p, @r and @s in the default Nukkit commands.
You can even include it in your own commands!

Default config (should come with the plugin): https://github.com/deminecrafterlol/Targets/blob/master/config.yml

Real world example:
```java
ArrayList<Entity> entities = Target.parse(args[0], sender);
for(Entity entity : entities) {
    if (entity instanceof Player) {
        Player player = (Player) entity;
        player.sendMessage("hello");
    }
}
```
