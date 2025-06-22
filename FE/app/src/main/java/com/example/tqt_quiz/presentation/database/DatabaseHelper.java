package com.example.tqt_quiz.presentation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quiz.db";
    public static final int DATABASE_VERSION = 1;

    // Bảng Quiz
    public static final String TABLE_QUIZ = "Quiz";
    public static final String COLUMN_QUIZ_ID = "id";
    public static final String COLUMN_QUIZ_NAME = "name";
    public static final String COLUMN_QUIZ_DESCRIPTION = "description";
    public static final String COLUMN_QUIZ_START_TIME = "start_time";
    public static final String COLUMN_QUIZ_DUE_TIME = "due_time";
    public static final String COLUMN_QUIZ_IS_PUBLIC = "is_public";

    // Bảng Question
    public static final String TABLE_QUESTION = "Question";
    public static final String COLUMN_QUESTION_ID = "id";
    public static final String COLUMN_QUESTION_QUIZ_ID = "quiz_id";
    public static final String COLUMN_QUESTION_CONTENT = "content";

    // Bảng Answer
    public static final String TABLE_ANSWER = "Answer";
    public static final String COLUMN_ANSWER_ID = "id";
    public static final String COLUMN_ANSWER_QUESTION_ID = "question_id";
    public static final String COLUMN_ANSWER_CONTENT = "content";
    public static final String COLUMN_ANSWER_IS_CORRECT = "is_correct";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuizTable = "CREATE TABLE " + TABLE_QUIZ + " (" +
                COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUIZ_NAME + " TEXT, " +
                COLUMN_QUIZ_DESCRIPTION + " TEXT, " +
                COLUMN_QUIZ_START_TIME + " TEXT, " +
                COLUMN_QUIZ_DUE_TIME + " TEXT, " +
                COLUMN_QUIZ_IS_PUBLIC + " INTEGER)";

        String createQuestionTable = "CREATE TABLE " + TABLE_QUESTION + " (" +
                COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION_QUIZ_ID + " INTEGER, " +
                COLUMN_QUESTION_CONTENT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_QUESTION_QUIZ_ID + ") REFERENCES " +
                TABLE_QUIZ + "(" + COLUMN_QUIZ_ID + ") ON DELETE CASCADE)";

        String createAnswerTable = "CREATE TABLE " + TABLE_ANSWER + " (" +
                COLUMN_ANSWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ANSWER_QUESTION_ID + " INTEGER, " +
                COLUMN_ANSWER_CONTENT + " TEXT, " +
                COLUMN_ANSWER_IS_CORRECT + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ANSWER_QUESTION_ID + ") REFERENCES " +
                TABLE_QUESTION + "(" + COLUMN_QUESTION_ID + ") ON DELETE CASCADE)";

        db.execSQL(createQuizTable);
        db.execSQL(createQuestionTable);
        db.execSQL(createAnswerTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }

    public long insertQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_QUIZ_NAME, quiz.getName());
        values.put(COLUMN_QUIZ_DESCRIPTION, quiz.getDescription());
        values.put(COLUMN_QUIZ_START_TIME, quiz.getStartTime());
        values.put(COLUMN_QUIZ_DUE_TIME, quiz.getDueTime());
        values.put(COLUMN_QUIZ_IS_PUBLIC, quiz.isPublished() ? 1 : 0);

        return db.insert(TABLE_QUIZ, null, values);
    }

    public long insertQuestion(long quizId, Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_QUESTION_QUIZ_ID, quizId);
        values.put(COLUMN_QUESTION_CONTENT, question.getContent());

        return db.insert(TABLE_QUESTION, null, values);
    }

    public long insertAnswer(long questionId, Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ANSWER_QUESTION_ID, questionId);
        values.put(COLUMN_ANSWER_CONTENT, answer.getContent());
        values.put(COLUMN_ANSWER_IS_CORRECT, answer.isCorrect() ? 1 : 0);

        return db.insert(TABLE_ANSWER, null, values);
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZ, null, null, null, null, null, COLUMN_QUIZ_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_DESCRIPTION));
                String start = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_START_TIME));
                String due = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_DUE_TIME));
                boolean isPublic = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_IS_PUBLIC)) == 1;

                Quiz quiz = new Quiz("", name, description, start, due, isPublic);
                quizList.add(quiz);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return quizList;
    }

}
