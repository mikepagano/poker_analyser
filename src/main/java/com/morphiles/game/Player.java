package com.morphiles.game;

import java.util.ArrayList;

public class Player {
	
	private String playerId;
	private float stack;
	private int seatId;
	private Card[] holeCards;
	private Hand hand;

	private static String currency;

    private static String BB_amount;
    private static String SB_amount;
    private static String ante;

    private static String date;
    private static String time;
    private static String limitType;

	private float[] contributions;
	private String[] actions;
	private float profitAmount;
	
	private static int[] playersPerRound;
	private static int tablePlayerCount;
	private static int totalSeatCount;
	
	private static int buttonSeat;

    private String preflopHand;
	
	private boolean isBigBlind;
	private boolean isSmallBlind;
	private boolean isWinner;
	
	private boolean hasLeft; // 1 - leaves table
	private boolean hasJoined; // 1 - new joiner 
	
	
	// the variables below belong to all players so are staic.
	// this makes updating them easier.
	private static float[] totalPot;
	private float[] myPot;
	
	// belong to all players hands.
	private static ArrayList<Card> communityCards;
	
	// this array stores the nut hand for each round.
	// only shows for the flop the turn and the river.
	public static String[] nutsForRound;
	
	
	public Player(int seatId){
		this.seatId = seatId;
		playerId = "";
		stack = 0.0f;
		currency="";
		communityCards = new ArrayList<Card>();
		
		initTotals();
	}
	
	// not really used except in the games.
	public Player(String name, float stack, int seatId, String currency){
		this.playerId = name;
		this.stack = stack;
		this.seatId = seatId;
		this.currency = currency;
		communityCards = new ArrayList<Card>();
		
		initTotals();
	}
	
	public Card[] getHoleCards()
	{
		return holeCards;
	}
	
	public String getHoleCardsAsString()
	{
		String temp = "";
		if (holeCards!=null){
			temp = holeCards[0].toString() + " " + holeCards[1].toString();
		} else {
			temp = "";
		}
		return temp;
	}

	public void setHoleCards(Card[] holeCards)
	{
		this.holeCards = holeCards;
        this.preflopHand = Hand.getPreflopHandType(holeCards);
	}
	
	public String getPlayerId()
	{
		return playerId;
	}
	
	public void setPlayerId(String playerId)
	{
		this.playerId = playerId;
	}
	public float getStack()
	{
		return stack;
	}
	public void setStack(float stack)
	{
		this.stack = stack;
	}
	public int getSeatId()
	{
		return seatId;
	}
	public void setSeatId(int seatId)
	{
		this.seatId = seatId;
	}
	
	public String getCurrency()
	{
		return currency;
	}
	
	public void setCurrency(String curr)
	{
		this.currency = curr;
	}
	
	public void updateContributions(int round, float amount)
	{
		// Contributions is calculated per round.
		contributions[round] = contributions[round] + amount;
		
		// total pot is the pot total after each round and includes
		// bets from all previous rounds
		if (totalPot[round] == 0.0f && round != 0)
		{
			totalPot[round] = totalPot[round-1] + amount;
		}
		else
		{
			totalPot[round] = totalPot[round] + amount;
		}
		
		// profitAmount is a total of all contributions to a pot
		// and will be used at the end to calculate any overall profit
		profitAmount = profitAmount - amount;
		
		myPot[round] = totalPot[round];

	}
	
	public float getContributions(int round)
	{
		return contributions[round];
	}
	
	public void updateActions(int round, String data)
	{
		if (actions[round] == "")
		{
			actions[round] = getAction(round, data).replace(",","");
		}
		else 
		{
			actions[round] = actions[round] + "-" + getAction(round, data).replace(",","");
		}
	}
	
	public String getActions(int round)
	{
		for (int i=0; i<round; i++){
			if (actions[i].contains("all-In"))
			{
				// all in will be the last move so just take
				// substring from position of 'all-in' to end
				if (playersPerRound[round]>=2){
					return actions[i].substring(actions[i].indexOf("all-In"));
				}
				else {
					return "";
				}
			}
		}
		if (actions[round] == null){
			return "";
		}
		return actions[round];
	}
	
	public float getTotalPot(int round)
	{
		if (round==0)
		{
			if (actions[round].contains("all-In"))
			{
				if (playersPerRound[round]>=2)
				{
					return myPot[round];
				}
				else
				{
					return 0.0f;
				}
			}
		}
		else
		{
			for (int i=0; i<=round; i++)
			{
				if (actions[i].contains("all-In") && playersPerRound[round]>=2)
				{
					// all in will be the last move so just take
					// substring from position of 'all-in' to end
					return myPot[i];
				}
			}
		}
		
		// If there's no all in continue to process data so 
		// 0 output goes out.
		if (actions[round].contains("fold") || actions[round].equals(""))
		{
			return 0.0f;
		} 
		else
		{
			return totalPot[round];
		}
	}
	
	public String getAction(int round, String data)
	{
		String thisAction= "";
		if (data.contains(" folds"))
		{
			thisAction = "fold";
			updatePlayerCount(round);
		}
		if (data.contains(" checks"))
		{
			thisAction = "check";
		}
		if (data.contains(" calls "))
		{
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" bets "))
		{
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" raises "))
		{
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" is all-In "))
		{
			thisAction = data.substring(data.indexOf(" ",  data.indexOf(" ", data.indexOf("is")+2))+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
        if (data.contains("posts big blind + dead ")){
            thisAction = data.substring(data.lastIndexOf(" ")).replace("[","").replace("].","").replace("]","").trim();
            if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
            {
                thisAction = thisAction.substring(1);
                thisAction = thisAction.substring(0);
                if (thisAction.contains(" ")){
                    thisAction = thisAction.substring(0, thisAction.indexOf(" "));
                }
            }
            thisAction = "BB+DB " + thisAction;
        } else if (data.contains("posts big blind")){
			thisAction = data.substring(data.indexOf(" posts big blind ")+17).replace("[","").replace("].","").replace("]","").trim();
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
                thisAction = thisAction.substring(1, thisAction.indexOf(" "));
				if(thisAction.contains(" ")){
                    thisAction = thisAction.substring(0, thisAction.indexOf(" "));
                }
			}
			thisAction = "BB " + thisAction; 
		}
		if (data.contains("posts small blind")){
			thisAction = data.substring(data.lastIndexOf(" ")+1).replace("[","").replace("]","").trim();
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€"))
			{
				thisAction = thisAction.substring(0, thisAction.length()-5);
			}
			thisAction = "SB " + thisAction;
		}
		
		return thisAction;
	}
	
	public void setCommunityCards(int round, String data)
	{
		if (round == 1)
		{
			String tempFlop = data.substring(data.indexOf("[")+1, data.indexOf("]")).replace(",","").trim();
			
			communityCards.add(new Card(tempFlop.substring(0, tempFlop.indexOf(" "))));
			communityCards.add(new Card(tempFlop.substring(tempFlop.indexOf(" ")+1,tempFlop.lastIndexOf(" "))));
			communityCards.add(new Card(tempFlop.substring(tempFlop.lastIndexOf(" ")+1)));
		}
		else // turn & river. - not same for both - hence Trim(). difference = 1.
		{
			String temp = data.substring(data.indexOf("[")+1,data.indexOf("]")).trim();
			communityCards.add(new Card(temp));
		}
	}
	
	public String getCommunityCards(int round)
	{
		String cardString = "";
		
		switch(round){
			case(1):
				if (communityCards.size()>=3)
				{
					cardString = communityCards.get(0) + ", "
							+ communityCards.get(1) + ", "
							+ communityCards.get(2);
				}
				break;
			case(2):
				if (communityCards.size()>=4)
				{
					cardString = communityCards.get(3) + "";
				}
				break;
			case(3):
				if (communityCards.size()>=5)
				{
					cardString = communityCards.get(4) + "";
				}
				break;
		}
		
		return cardString;
	}
	
	public void setTotalPlayerCount(int value)
	{
		playersPerRound[0] = value;
		playersPerRound[1] = value;
		playersPerRound[2] = value;
		playersPerRound[3] = value;
		
		tablePlayerCount = value;
		
		// these will be updated per round...
		// this will happen by reducing all subsequent rounds 
		// by 1 player each time a player folds.
	}
	
	public static void updatePlayerCount(int round)
	{
		for (int i=3; i>=round; i--)
		{
			playersPerRound[i] -= 1;
		}
	}
	
	public String getPlayerCountForRound(int round)
	{
		for (int i=0; i<round; i++)
		{
			if (playersPerRound[i]<=1){
				return "";
			}
		}
		return playersPerRound[round]+"";
	}
	
	public int getTablePlayerCount()
	{
		return tablePlayerCount;
	}
	
	public String getSeatPosition(int seat)
	{
		//TODO double check this is correct
		String position = "-1";
		if (seat == buttonSeat)
		{
			position = "BTN";
		}
		else if (isBigBlind)
		{
			position = "BB";
		}
		else if (isSmallBlind)
		{
			position = "SB";
		}
		else
		{
			position = "" + (seat%(tablePlayerCount+1));
		}
		return position;
	}
	
	public int getButtonSeat()
	{
		return this.buttonSeat;
	}
	
	public void setButtonSeat(int value)
	{
		this.buttonSeat = value;
	}
	
	public int getSeatCount()
	{
		return this.totalSeatCount;
	}
	
	public void setTotalSeatCount(int value)
	{
		this.totalSeatCount = value;
	}
	
	public void setBigBlind(boolean isBB)
	{
		isBigBlind = isBB;
	}

    public boolean isBigBlind()
    {
        return isBigBlind;
    }
	
	public void setSmallBlind(boolean isSB)
	{
		isSmallBlind = isSB;
	}
	
	public boolean isSmallBlind()
	{
		return isSmallBlind;
	}
	
	public void updateProfitAmount(float amount){
		profitAmount = profitAmount + amount;
	}
	
	
	public float getProfit(){
		return profitAmount;
	}
	
	public void setWinner(boolean win)
	{
		isWinner = win;
	}
	
	public void updateTimeBank(int round)
	{
		if (actions[round].equals(""))
		{
			actions[round] = "(T)";
		}
		else
		{
			actions[round] = actions[round] + "-(T)";
		}
	}
	
	public String getWinner()
	{
		if (isWinner)
		{
			return "Win";
		} 
		else
		{
			return "";
		}
	}
	
	public void setLeftTable(boolean left)
	{
		hasLeft = left;
	}
	
	public void setJoinedTable(boolean joined)
	{
		hasJoined = joined;
	}
	
	
	public String getLeftTable()
	{
		if (hasLeft)
		{
			return "Left";
		}
		else if (hasJoined){
			return "Joined";
		}
		else
		{
			return "";
		}
	}
	
	
	public Card[] getCardsUpto(int round){
		Card[] cs = {};
		switch(round)
		{
			case(1):
				cs = new Card[3];
				break;
			case(2):
				cs = new Card[4];
				break;
			case(3):
				cs = new Card[5];
				break;
		}		
		
		for (int i=0; i<cs.length; i++)
		{
			cs[i] = communityCards.get(i);
		}
		
		return cs;
	}
	
	public Card[] getCardsUptoTurn()
	{
		Card[] t = new Card[4];
		t[0] = communityCards.get(0);
		t[1] = communityCards.get(1);
		t[2] = communityCards.get(2);
		t[3] = communityCards.get(3);
		return t;
	}

    public String getPreflopHand(){
        return preflopHand;
    }
	
	public void initTotals()
	{
		contributions = new float[4];
		actions = new String[4];
		totalPot = new float[4];
		myPot = new float[4];
		playersPerRound = new int[4];
				
		reset();
	}
	
	public void reset() 
	{
		communityCards.clear();
		buttonSeat = -1;
		tablePlayerCount = -1;
		totalSeatCount = -1;
		isBigBlind = false;
		isSmallBlind = false;
		isWinner = false;
		hasLeft = false;
        preflopHand = "";
		
		
		for (int i=0; i<4; i++)
		{
			contributions[i] = 0.0f;
			actions[i] = "";
			totalPot[i] = 0.0f;
			myPot[i] = 0.0f;
			playersPerRound[i] = 0;
		}	
	}





}