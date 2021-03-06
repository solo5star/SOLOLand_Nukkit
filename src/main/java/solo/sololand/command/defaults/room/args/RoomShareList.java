package solo.sololand.command.defaults.room.args;

import cn.nukkit.Server;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import solo.sololand.command.SubCommand;
import solo.sololand.world.World;
import solo.sololand.land.Land;
import solo.sololand.land.Room;
import solo.solobasepackage.util.Message;

import java.util.ArrayList;

public class RoomShareList extends SubCommand{

	public RoomShareList(){
		super("공유목록", "공유받은 방 목록을 확인합니다.");
		this.setPermission("sololand.command.room.list");
		this.addCommandParameters(new CommandParameter[]{
			new CommandParameter("페이지", CommandParameter.ARG_TYPE_INT, true)
		});
		this.addCommandParameters(new CommandParameter[]{
			new CommandParameter("유저", CommandParameter.ARG_TYPE_STRING, true),
			new CommandParameter("페이지", CommandParameter.ARG_TYPE_INT, true)
		});
	}

	@Override
	public boolean execute(CommandSender sender, String[] args){
		Player player = (Player) sender;
		String targetName = player.getName();
		int page = 1;
		if(args.length == 1){
			try{
				page = Integer.parseInt(args[0]);
			}catch(Exception e){
				Player target = Server.getInstance().getPlayer(args[0]);
				if(target != null){
					targetName = target.getName();
				}else{
					targetName = args[0];
				}
			}
		}else if(args.length > 1){
			Player target = Server.getInstance().getPlayer(args[0]);
			if(target != null){
				targetName = target.getName();
			}else{
				targetName = args[0];
			}
			try{
				page = Integer.parseInt(args[1]);
			}catch(Exception e){
				
			}
		}
		ArrayList<String> information = new ArrayList<String>();
		for(World world : World.getAll().values()){
			for(Land land : world.getLands().values()){
				if(land.hasRoom()){
					for(Room room : land.getRooms().values()){
						if(room.isMember(player) && ! room.isOwner(player)){
							String line = "§l§a[" + world.getCustomName() + " 월드] " + room.getOwner() + "님의 "+ room.getAddress() + "번방 §r§7";
							if(! room.getWelcomeMessage().equals("")){
								line += " - " + room.getWelcomeMessage();
							}
							information.add(line);
						}
					}
				}
			}
		}
		if(information.size() == 0){
			Message.normal(player, targetName + "님은 공유받은 방이 없습니다.");
		}else{
			Message.page(player, targetName + "님의 공유받은 방 목록", information, page);
		}
		return true;
	}
}