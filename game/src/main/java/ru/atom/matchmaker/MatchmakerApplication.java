package ru.atom.matchmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.atom.matchmaker.players.dao.PlayerDao;
import ru.atom.matchmaker.players.model.Player;
import java.util.ArrayList;
import java.util.List;*/

@SpringBootApplication
public class MatchmakerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);


/*		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/beans.xml");
		PlayerDao playerDao = context.getBean(PlayerDao.class);
		Player player = new Player("alexey", "1234");
		PlayerDao.save(player);
		System.out.println("Player::" + player);
		List<Player> list = playerDao.getPlayerList();
		for(Player p : list) {
			System.out.println("Player List::" + p);
		}
		context.close();*/


	}
}
