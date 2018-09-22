package Main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import ascii.ASCII;
import events.Dice;
import events.Turn;
import maps.MapReader;
import maps.WorldMap;
import player.Player;
import territories.Territory;

/**
 * Game class manipulates all Risk objects to be called in main
 * @author Grant Williams
 * 
 * @date 9/4/18
 **/

public class Game {
	
	private Scanner userInput;
	private MapReader map;
	private ArrayList<Player> players;
	private ArrayList<Territory> wTerritories;
	private boolean validInput;
	private Dice dice;
	private Turn turn;
	private ASCII ascii;
	private MapReader mapReader;
	private WorldMap world;
	
	public Game(){
		userInput = new Scanner(System.in);
		map = new MapReader();
		players = new ArrayList<Player>();
		wTerritories = new ArrayList<Territory>();
		validInput = false;
		dice = new Dice();
		turn = new Turn();
		ascii = new ASCII();
		mapReader = new MapReader();
		world = new WorldMap();
	}
	
	public void run(){
		//Display Banner
		ascii.readASCII("C:\\Users\\grant\\Desktop\\School\\COSC\\COSC 4353\\Projects\\Risk Game\\myRiskGame\\src\\ascii\\asciiTitle");
		
		System.out.println("GAME SETUP:");
		System.out.println("+=============================================================================================================================================================+");
		
		//Setting up players
		getPlayerInfo();
		
		//Setting up world map
		System.out.println("---------------------------------------------------------------------");
		//mapReader.readInMap("C:\\Users\\grant\\Desktop\\School\\COSC\\COSC 4353\\Projects\\Risk Game\\myRiskGame\\src\\maps\\worldmap");
		//mapReader.printWorldMap();
		//System.out.println("Map Created...");
		
		world.createWorldMap();
		//world.printWorldMap();
		System.out.println("World Created...");
		System.out.println("---------------------------------------------------------------------");		
		//Distribute territories of world map to players
		distributeTerritories();
		System.out.println("Territories Distributed...");
		System.out.println("---------------------------------------------------------------------");
		
		System.out.println("GAME SETUP COMPLETE");
		System.out.println("+=============================================================================================================================================================+");;
		
		//Turn iteration
		runOneTurn(1);
		//runOneTurn(2);
		
		//End of Game run function
		userInput.close();
	}
	
	//Gets the number of players from the user
	private int getPlayerCount(){
		validInput = false;
		String in = "";
		int numPlayers = 0;
		while(!validInput){
			System.out.println("How many players?(2, 3, 4, 5, or 6)");
			in = userInput.nextLine();
			if(!(in.equals("2") || in.equals("3") || in.equals("4") || in.equals("5") || in.equals("6"))){
				System.out.println("---------------------------------------------------------------------");
				System.out.println("Invalid input. Please try again.");
				System.out.println("---------------------------------------------------------------------");
				}
			else{
				numPlayers = Integer.parseInt(in);
				System.out.println("---------------------------------------------------------------------");
				validInput = true;
			}
		}
		return numPlayers;
	}
	
	//Returns amount of troops to give each player upon construction
	private int getTroopCount(int numPlayers){
		if(numPlayers == 2){
			return 40;
		}
		else if(numPlayers == 3){
			return 35;
		}
		else if(numPlayers == 4){
			return 30;
		}
		else if(numPlayers == 5){
			return 25;
		}
		else{
			return 20;
		}
	}
	
	//Gets the names of the players from the user
	private void getPlayerInfo(){
		validInput = false;
		String in = "";
		int numPlayers = getPlayerCount();
		int numTroops = getTroopCount(numPlayers);
		for(int i = 1; i <= numPlayers; i++){
			System.out.println("Enter Name for Player " + i + ": ");
			in = userInput.nextLine();
			Player temp = new Player(in, numTroops, i);
			players.add(temp);
			System.out.println("---------------------------------------------------------------------");
			}
		for(int i = 0; i < players.size(); i++){
			players.get(i).printPlayer();
		}
	}
	
	//Returns a deep copy of a given ArrayList of Territories
	private ArrayList<Territory> getDeepCopy(ArrayList<Territory> original){
		ArrayList<Territory> copy = new ArrayList<Territory>();
		for(int i = 0; i < original.size(); i++){
			copy.add(original.get(i));
		}
		return copy;
	}
	
	//Distributes all territories amongst amount of players
	//To be called after player information is obtained and WorldMap created
	private void distributeTerritories(){
		int playerCount = players.size();
		//wTerritories = getDeepCopy(map.getMapTerritories());
		wTerritories = getDeepCopy(world.getWorldTerritories());
		int player = 1;
		Random random = new Random();
		while(wTerritories.size() > 0){
			if(wTerritories.size() < playerCount){
				while(wTerritories.size() > 0){
					int nextTerritory = random.nextInt(wTerritories.size());
					Territory tempT = wTerritories.get(nextTerritory);
					Player tempP = players.get(player-1);
					tempP.addTerritory(tempT);
					tempP.decreaseTroops(1);
					tempT.setOccupant(tempP);
					tempT.increaseTroopCount(1);
					wTerritories.remove(nextTerritory);
					player++;
				}
			}
			else{
				while(player <= playerCount){
					int nextTerritory = random.nextInt(wTerritories.size());
					Territory tempT = wTerritories.get(nextTerritory);
					Player tempP = players.get(player-1);
					tempP.addTerritory(tempT);
					tempP.decreaseTroops(1);
					tempT.setOccupant(tempP);
					tempT.increaseTroopCount(1);
					wTerritories.remove(nextTerritory);
					player++;
				}
				player = 1;
			}
		}
	}
	
	//Runs one cycle of a turn between all players
	private void runOneTurn(int turnCount){
		turn.runTurn(players, turnCount, getDeepCopy(world.getWorldTerritories()));
	}
	
}
