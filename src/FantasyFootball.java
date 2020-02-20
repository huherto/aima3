import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FantasyFootball {
  
  enum Team {
    BYE_WEEK,
    DALLAS,
    GARBAGE,

    OUT_OF_THE_WOODS,
    GRONK,
    JAZZY,
    BABYS,
    MANIPON,
    PACKHUNTERS,
    GROUND;
  };
  
  public static class Game {
    
    Team team1;
    int score1;
    Team team2;
    int score2;
    
    public Game(Team team1, int score1, Team team2, int score2) {
      this.team1 = team1;
      this.score1 = score1;
      this.team2 = team2;
      this.score2 = score2;
    }
    
    public Game(Team team1, Team team2) {
      this.team1 = team1;
      this.score1 = 0;
      this.team2 = team2;
      this.score2 = 0;
    }

    public Team winner() {
      return score1 > score2 ? team1 : team2;
    }

    public Object loser() {
      return score1 <= score2 ? team1 : team2;    
    }
    
  }
  
  public static class TeamStats implements Comparable<TeamStats> {
    
    Team team;
    int wins = 0;
    int losses = 0;
    
    double mean;
    double stdError;
    
    TeamStats(Team team, List<Game> pastGames) {
      this.team = team;
      
      List<Integer> scores = new ArrayList<Integer>();
      for(Game g: pastGames) {
        if (g.team1.equals(team)) {
          scores.add(g.score1);
        }
        if (g.team2.equals(team)) {
          scores.add(g.score2);        
        }
      }
      
      int min = scores.get(0);
      int max = scores.get(0);
      int count = 0;
      int sum = 0;
      for(int i = 1; i < scores.size(); i++) {
        min = Math.min(min, scores.get(i));
        max = Math.max(max, scores.get(i));
        count++;
        sum += scores.get(i);
      }
      
      mean = (1.0f*sum)/count;
      double var = 0;
      for(int i = 1; i < scores.size(); i++) {
        double d = scores.get(i) - mean;
        var += d * d;    
      }
      
      stdError = Math.sqrt(var/(count-1));
    }
    
    public int getProjection() {
      switch(team) {
        case BYE_WEEK: return 114;
        case DALLAS: return 121;
        case GARBAGE: return 123;
        case PACKHUNTERS: return 132;
        case OUT_OF_THE_WOODS: return 100;
        case GRONK: return 127;
        case JAZZY: return 121;
        case BABYS: return 127;
        case MANIPON: return 132;
        case GROUND: return 108;
      }
      throw new RuntimeException("It broke!");
    }
    
    @Override
    public String toString() {
      return String.format("team=%s, wins=%d, losses=%d, mean=%.2f, proj=%d, stdError=%.2f", team, wins, losses, mean, getProjection(), stdError);
    }
    
    public int getWins() {
      return wins;
    }
    
    public int getLosses() {
      return losses;
    }

    @Override
    public int compareTo(TeamStats t) {
      return this.wins - t.wins;
    }

    public int predictScore() {
      double point = (mean + getProjection())/2.0;
      return (int)(Math.round((rand.nextGaussian() * stdError) + getProjection()));
    }
    
  }
  
  public List<TeamStats> simulateTournament() throws IOException {
    
    List<Game> pastGames = loadPastGames();
    
    Map<Team, TeamStats> allstats = loadTeamStats(pastGames);
    
    
    File f = File.createTempFile("", "");
    
    f.getAbsolutePath();
    f.deleteOnExit();
    f.delete();
    
    String v = new String ( java.nio.file.Files.readAllBytes( java.nio.file.Paths.get(f.toURI()) ) );
    
    
    List<Game> futureGames = loadFutureGames();

    for(Game game : futureGames) {
      game.score1 =  allstats.get(game.team1).predictScore();
      game.score2 =  allstats.get(game.team2).predictScore();
    }
    
    List<Game> allGames = new ArrayList<>();
    allGames.addAll(pastGames);
    allGames.addAll(futureGames);
    
    for(Game game : allGames) {
      allstats.get(game.winner()).wins++;
      allstats.get(game.loser()).losses++;
    }
    
    List<TeamStats> res = new ArrayList<>(allstats.values());
    Collections.shuffle(res);
    Collections.sort(res);
    Collections.reverse(res);
    return res;
  }

  private Map<Team, TeamStats> loadTeamStats(List<Game> pastGames) {
    Map<Team, TeamStats> allstats = new HashMap<>();
    for(Team team : Team.values()) {
      allstats.put(team, new TeamStats(team, pastGames));
    }
    return allstats;
  }
  
  private void printTeamStats(List<TeamStats> list) {
    for(TeamStats teamStats : list) {
      println(teamStats.toString());
    }
    println("");
  }

  private List<Game> loadPastGames() {
    List<Game> pastGames = new ArrayList<>();   
    // week 1
    pastGames.add(new Game(Team.BYE_WEEK, 99, Team.DALLAS, 136));
    pastGames.add(new Game(Team.GARBAGE, 125, Team.PACKHUNTERS, 160));
    pastGames.add(new Game(Team.OUT_OF_THE_WOODS, 112, Team.GRONK, 102));
    pastGames.add(new Game(Team.JAZZY, 126, Team.BABYS, 161));
    pastGames.add(new Game(Team.MANIPON, 121, Team.GROUND, 126));
    
    // week 2
    pastGames.add(new Game(Team.DALLAS,102,Team.JAZZY,107));
    pastGames.add(new Game(Team.OUT_OF_THE_WOODS,80,Team.GARBAGE,114));
    pastGames.add(new Game(Team.BYE_WEEK,121,Team.MANIPON,123));
    pastGames.add(new Game(Team.PACKHUNTERS,146,Team.BABYS, 135));
    pastGames.add(new Game(Team.GRONK,155,Team.GROUND,91));

    // week 3
    pastGames.add(new Game(Team.PACKHUNTERS,152,Team.DALLAS,98));
    pastGames.add(new Game(Team.MANIPON,134,Team.JAZZY,123));
    pastGames.add(new Game(Team.BABYS,153,Team.GARBAGE,120));
    pastGames.add(new Game(Team.GROUND,131,Team.OUT_OF_THE_WOODS,146));
    pastGames.add(new Game(Team.GRONK,117,Team.BYE_WEEK, 82));

    // week 4
    pastGames.add(new Game(Team.GARBAGE, 110, Team.DALLAS, 124));
    pastGames.add(new Game(Team.GROUND, 101,Team.BABYS, 108));
    pastGames.add(new Game(Team.JAZZY, 90,Team.PACKHUNTERS, 166));
    pastGames.add(new Game(Team.MANIPON,125,Team.GRONK,127));
    pastGames.add(new Game(Team.OUT_OF_THE_WOODS, 132,Team.BYE_WEEK, 103));
    
    // week 5
    pastGames.add(new Game(Team.DALLAS,122,Team.BABYS,187));
    pastGames.add(new Game(Team.GRONK,146,Team.PACKHUNTERS,143));
    pastGames.add(new Game(Team.BYE_WEEK,122,Team.GROUND,169));
    pastGames.add(new Game(Team.GARBAGE,105,Team.JAZZY,146));
    pastGames.add(new Game(Team.OUT_OF_THE_WOODS, 96,Team.MANIPON, 110));
    return pastGames;
  }
  
  private List<Game> loadFutureGames() {
    List<Game> futureGames = new ArrayList<>();
    
    // week 6
    futureGames.add(new Game(Team.DALLAS,Team.GRONK));
    futureGames.add(new Game(Team.GARBAGE,Team.BYE_WEEK));
    futureGames.add(new Game(Team.JAZZY,Team.OUT_OF_THE_WOODS));
    futureGames.add(new Game(Team.BABYS,Team.MANIPON));
    futureGames.add(new Game(Team.PACKHUNTERS,Team.GROUND));
    
    // week 7
    futureGames.add(new Game(Team.GROUND,Team.DALLAS));
    futureGames.add(new Game(Team.BYE_WEEK,Team.JAZZY));
    futureGames.add(new Game(Team.OUT_OF_THE_WOODS,Team.BABYS));
    futureGames.add(new Game(Team.MANIPON,Team.PACKHUNTERS));
    futureGames.add(new Game(Team.GRONK,Team.GARBAGE));
    
    // week 8
    futureGames.add(new Game(Team.DALLAS,Team.MANIPON));
    futureGames.add(new Game(Team.BABYS,Team.BYE_WEEK));
    futureGames.add(new Game(Team.PACKHUNTERS,Team.OUT_OF_THE_WOODS));
    futureGames.add(new Game(Team.GARBAGE,Team.GROUND));
    futureGames.add(new Game(Team.JAZZY,Team.GRONK));
    
    // week 9
    futureGames.add(new Game(Team.BABYS,Team.DALLAS));
    futureGames.add(new Game(Team.BYE_WEEK,Team.PACKHUNTERS));
    futureGames.add(new Game(Team.GRONK,Team.OUT_OF_THE_WOODS));
    futureGames.add(new Game(Team.JAZZY,Team.GARBAGE));
    futureGames.add(new Game(Team.GROUND,Team.MANIPON));
    
    // week 10
    futureGames.add(new Game(Team.OUT_OF_THE_WOODS,Team.DALLAS));
    futureGames.add(new Game(Team.PACKHUNTERS,Team.GARBAGE));
    futureGames.add(new Game(Team.MANIPON,Team.BYE_WEEK));
    futureGames.add(new Game(Team.BABYS,Team.JAZZY));
    futureGames.add(new Game(Team.GROUND,Team.GRONK));
    
    // week 11
    futureGames.add(new Game(Team.JAZZY,Team.DALLAS));
    futureGames.add(new Game(Team.MANIPON,Team.GARBAGE));
    futureGames.add(new Game(Team.OUT_OF_THE_WOODS,Team.GROUND));
    futureGames.add(new Game(Team.BABYS,Team.PACKHUNTERS));
    futureGames.add(new Game(Team.BYE_WEEK,Team.GRONK));
    
    // week 12
    futureGames.add(new Game(Team.DALLAS,Team.PACKHUNTERS));
    futureGames.add(new Game(Team.GROUND,Team.JAZZY));
    futureGames.add(new Game(Team.GARBAGE,Team.BABYS));
    futureGames.add(new Game(Team.GRONK,Team.MANIPON));
    futureGames.add(new Game(Team.BYE_WEEK,Team.OUT_OF_THE_WOODS));
    return futureGames;
  }

  public static void println(String format, Object...args) {
    System.out.println(String.format(format, args));
  }
  
  public static void print(String format, Object...args) {
    System.out.println(String.format(format, args));
  }

  static Random rand = new Random();

  public static class AllTimeStats {
    public AllTimeStats(Team team) {
      this.team = team;
    }
    Team team;
    int firstPlaceCount = 0;
    int lastPlaceCount = 0;
  }
  
  public static class FirstPlaceComparator implements Comparator<AllTimeStats> {
    @Override
    public int compare(AllTimeStats o1, AllTimeStats o2) {
      return o1.firstPlaceCount - o2.firstPlaceCount;
    }
  };
    
  public static class LastPlaceComparator implements Comparator<AllTimeStats> {
    @Override
    public int compare(AllTimeStats o1, AllTimeStats o2) {
      return o1.lastPlaceCount - o2.lastPlaceCount;
    }
  };
  
  public static void main(String[] args) throws IOException {

    {
      FantasyFootball ff = new FantasyFootball();
      ff.printInitialTeamStats();
    }

    Map<Team, AllTimeStats> all = new HashMap<>();
    for(Team team : Team.values()) {
      all.put(team, new AllTimeStats(team));
    }

    int NUM_TOURNAMENTS = 1000_000;
    for(int i = 0; i < NUM_TOURNAMENTS; i++) {
      FantasyFootball ff = new FantasyFootball();
      List<TeamStats> res = ff.simulateTournament();
      Team winner = res.get(0).team;
      Team loser = res.get(res.size() - 1).team;
      all.get(winner).firstPlaceCount++;
      all.get(loser).lastPlaceCount++;
      
      if (winner.equals(Team.GARBAGE)) {
        //ff.printTeamStats(res);
      }
      
      checkIntegrity(res);
    }

    List<AllTimeStats> allTeams = new ArrayList<>(all.values());

    Collections.shuffle(allTeams);
    allTeams.sort(new FirstPlaceComparator().reversed());

    println("Champions");
    for(AllTimeStats team : allTeams) {
      double prob = (double)team.firstPlaceCount / NUM_TOURNAMENTS;
      println("team=%s, %.1f%% ", team.team, prob*100);
    }

    Collections.shuffle(allTeams);
    allTeams.sort(new LastPlaceComparator().reversed());
    println("");
    println("Anti-Champions");
    for(AllTimeStats team : allTeams) {
      double prob = (double)team.lastPlaceCount / NUM_TOURNAMENTS;
      println("team=%s, %.1f%%", team.team, prob*100);    
    }
  }

  private void printInitialTeamStats() {
    List<Game> pastGames = loadPastGames();
    Map<Team, TeamStats> allstats = loadTeamStats(pastGames);
    for(Game game : pastGames) {
      allstats.get(game.winner()).wins++;
      allstats.get(game.loser()).losses++;
    }
    List<TeamStats> list = new ArrayList<>(allstats.values());
    Collections.shuffle(list);
    Collections.sort(list);
    Collections.reverse(list);
    printTeamStats(list);
    println("");
  }

  private static void checkIntegrity(List<TeamStats> res) {
    int sumWins = 0;
    int sumLoses = 0;
    for(TeamStats ts : res) {
      sumWins += ts.wins;
      sumLoses += ts.losses;
      if (ts.wins + ts.losses != 12) {
        throw new RuntimeException("It is broken");
      }
    }
    if (sumWins != sumLoses) {
      throw new RuntimeException("It is broken");
    }
  }

}
