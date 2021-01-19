import org.bukkit.Bukkit
import org.bukkit.*
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class Main: JavaPlugin(), Listener {
    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    var kordy1: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    var kordy2: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    var kordy3: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    var kordy4: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    private fun add_itemy(p: Player) {
        val bedrock = ItemStack(Material.BEDROCK)
        val redstone = ItemStack(Material.REDSTONE_BLOCK)
        val meta_r = redstone.itemMeta
        val meta_b = bedrock.itemMeta
        bedrock.itemMeta = meta_b
        redstone.itemMeta = meta_r
        p.inventory.addItem(bedrock)
        p.inventory.addItem(redstone)
    }

    private fun remove_itemy(p: Player) {
        val bedrock = ItemStack(Material.BEDROCK)
        val redstone = ItemStack(Material.REDSTONE_BLOCK)
        val meta_r = redstone.itemMeta
        val meta_b = bedrock.itemMeta
        bedrock.itemMeta = meta_b
        redstone.itemMeta = meta_r
        p.inventory.remove(bedrock)
        p.inventory.remove(redstone)
    }

    override fun onCommand(

            sender: CommandSender?,
            command: Command?,
            label: String?,
            args: Array<out String>?

    ): Boolean {
        when (command?.name) {
            "kordyset" -> {
                if (sender is Player) {
                    val p: Player = sender
                    if ((args != null) && args.isNotEmpty()) {
                        when (args[0].toLowerCase()) {
                            "1" -> {
                                kordy1 = p.location
                                p.sendMessage(kordy1.toString())
                            }
                            "2" -> {
                                kordy2 = p.location
                                p.sendMessage(kordy2.toString())
                            }
                            "3" -> {
                                kordy3 = p.location
                                p.sendMessage(kordy3.toString())
                            }
                            "4" -> {
                                kordy4 = p.location
                                p.sendMessage(kordy4.toString())
                            }
                            "sprawdz" -> {
                                p.sendMessage("kordy 1: ${kordy1}")
                                p.sendMessage("kordy 2: ${kordy2}")
                                p.sendMessage("kordy 3: ${kordy3}")
                                p.sendMessage("kordy 4: ${kordy4}")
                            }
                        }

                    } else {
                        p.sendMessage("Bledne uzycie.")
                    }
                }
            }
        }
        return true
    }
    var player = HashMap<String, Int>()
    var czas_p = HashMap<String, Long>()
    var czas: Double = 0.0
    @EventHandler
    fun falling_off(e: PlayerMoveEvent) {
        val p = e.player
        if (czas_p.containsKey(p.name)) {
            czas = (((czas_p.get(p.name)!! / 10) - (System.currentTimeMillis() / 10)) * -1) / 100.0
        }
        if (p.location.y < 0 && player.containsKey(p.name) && player.containsValue(1)) {
            p.teleport(kordy1)
        }
        if (p.location.y < 0 && player.containsKey(p.name) && player.containsValue(2)) {
            p.teleport(kordy2)
        }
        if (p.location.y < 0 && player.containsKey(p.name) && player.containsValue(3)) {
            p.teleport(kordy3)
        }
        if (p.world.getBlockAt(p.location).location == p.world.getBlockAt(kordy1).location) {
            if ((player.containsKey(p.name)&&player.containsValue(0))) {
                    p.sendMessage("Parkour sie zaczal!")
                    p.sendMessage(player.toString())
                    player.put(p.name, 1)
                    p.sendMessage(player.toString())
                    czas_p.put(e.player.name, System.currentTimeMillis())
                    add_itemy(p)
                }
            else if(!player.containsKey(p.name)||!player.containsValue(0)&&!player.containsValue(1)&&!player.containsValue(2)&&!player.containsValue(3)){
                player.put(p.name, 0)
            }
        }
        if (p.world.getBlockAt(p.location).location == p.world.getBlockAt(kordy2).location) {
            if ((player.containsKey(p.name)&&player.containsValue(1))) {
                    p.sendMessage("Pierwszy checkpoint! Czas: ${czas}s")
                    p.sendMessage(player.toString())
                    player.put(p.name, 2)
                }
            }


        if (p.world.getBlockAt(p.location).location == p.world.getBlockAt(kordy3).location) {
            if ((player.containsKey(p.name)&&player.containsValue(2))) {
                    p.sendMessage("Drugi checkpoint! Czas: ${czas}s")
                    p.sendMessage(player.toString())
                    player.put(p.name, 3)
            }
        }
        if (p.world.getBlockAt(p.location).location == p.world.getBlockAt(kordy4).location) {
            if ((player.containsKey(p.name)&&player.containsValue(3))) {
                    p.sendMessage("Koniec! Czas: ${czas}s")
                    p.sendMessage(player.toString())
                    player.put(p.name, 0)
                    p.sendMessage(player.toString())
                    remove_itemy(p)
            }
        }
        @EventHandler
        fun parkour_i(e: PlayerInteractEvent) {
            val pl = e.player
            val action = e.action
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if (pl.itemInHand.type == Material.REDSTONE_BLOCK) {
                    pl.teleport(Location(pl.world, 90.5, 5.5, 102.5))
                    remove_itemy(p)
                    player.put(p.name, 0)
                    czas = 0.0
                    e.isCancelled = true
                }
                if (pl.itemInHand.type == Material.BEDROCK) {
                    player.put(p.name, 0)
                    e.isCancelled = true
                    remove_itemy(p)
                }
            }
        }

    }
}