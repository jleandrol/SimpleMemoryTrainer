package lao.simplememorytrainer;

/**
 * Created by leandro on 4/8/2017.
 */
public class HighScoreBean {

    private String name;
    private int score;
    private String date;

    public String getName() {
        return name;
    }

    public HighScoreBean setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public HighScoreBean setScore(int score) {
        this.score = score;
        return this;
    }

    public String getDate() {
        return date;
    }

    public HighScoreBean setDate(String date) {
        this.date = date;
        return this;
    }
}
