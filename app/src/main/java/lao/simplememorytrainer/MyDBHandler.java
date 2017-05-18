package lao.simplememorytrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leandro on 4/8/2017.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    static int DB_VERSION = 1;
    static String DB_NAME = "SMTDatabase.db";
    public static String TABLE_NAME = "highscores";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_SCORE = "score";
    public static String COLUMN_DATE = "date";

    private int QUANTITY_OF_HIGHSCORES = 10;

    private ArrayList <HighScoreBean> highScoresList = new ArrayList();

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_NAME + " TEXT , " +
                COLUMN_SCORE + " INT , " +
                COLUMN_DATE + " TEXT );" ;
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isNewHighScore(int newScore) {
        if ( newScore == 0 ) {
            return false;
        }
        fillScores();
        int count = 0;
        int lowerElementInHighScoreTopList = 0;
        if ( highScoresList.size() > 0 ) {
            lowerElementInHighScoreTopList = 999;
            for ( HighScoreBean bean : highScoresList ){
                count++;
                if ( lowerElementInHighScoreTopList > bean.getScore() ) {
                    lowerElementInHighScoreTopList = bean.getScore();
                }
                if ( count == QUANTITY_OF_HIGHSCORES) {
                    break;
                }
            }
        }
        if ( count != 0 && count != 10 ) {
            return true;
        }
        return ( lowerElementInHighScoreTopList < newScore );
    }

    public void deleteAllValues() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( " DELETE FROM " + TABLE_NAME);
    }

    public void addHighScore(String name, int score) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_DATE, getMonth());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    private String getMonth() {
        ArrayList <String> months  = new ArrayList();
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AGO");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = ""+calendar.get(Calendar.YEAR);
        return months.get( calendar.get(Calendar.MONTH)) + "." + year.substring(2) ;
    }

    private void fillScores(){
        highScoresList.clear();
        SQLiteDatabase db = getWritableDatabase();
        String sql = " SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC ";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        while ( !c.isAfterLast() ){

            highScoresList.add(
                    new HighScoreBean()
                            .setName(c.getString(c.getColumnIndex(COLUMN_NAME)))
                            .setScore(c.getInt(c.getColumnIndex(COLUMN_SCORE)))
                            .setDate(c.getString(c.getColumnIndex(COLUMN_DATE)))
            );
            c.moveToNext();
        }
        db.close();
    }

    public ArrayList<HighScoreBean> getHighScoresList() {
        fillScores();
        return highScoresList;
    }
}
