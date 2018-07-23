package com.jica.honeymorning.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jica.honeymorning.database.entity.CategoryValue;
import com.jica.honeymorning.database.entity.RecommendedList;
import com.jica.honeymorning.database.entity.TodoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static String PACKEGE = "com.jica.myapplication";
    static String DB_NAME = "scheduledata.db";
    static int VERSION  = 1;
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_table_todo = "CREATE TABLE todo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "note TEXT,"
                + "status INTEGER DEFAULT 1,"
                + "created_at DATE DEFAULT (datetime('now','localtime')))";
        String sql_table_category_info = "CREATE TABLE category_info ("
                + "id INTEGER PRIMARY KEY,"
                + "category_name TEXT,"
                + "status INTEGER DEFAULT 0)";
        String sql_table_recommend = "CREATE TABLE recommend ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "category_id INTEGER REFERENCES category_info(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                + "recommend_value TEXT,"
                + "status INTEGER DEFAULT 1)";
        db.execSQL(sql_table_todo);
        db.execSQL(sql_table_category_info);
        db.execSQL(sql_table_recommend);

        //초기 데이터
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (1,'운동')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (2,'일상')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (3,'상식')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (4,'병맛')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (5,'학교')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (6,'직장')" );
        db.execSQL( "INSERT INTO category_info (id,category_name) VALUES (7,'My')" );

        db.execSQL( "INSERT INTO todo (note) VALUES ('(잠금화면)왼쪽으로 밀어서 할일 삭제 가능')" );
        db.execSQL( "INSERT INTO todo (note) VALUES ('(잠금화면)오른쪽으로 밀어서 할일 완료 가능')" );
        db.execSQL( "INSERT INTO todo (note) VALUES ('(잠금화면)+버튼으로 당일 메모 가능')" );
        db.execSQL( "INSERT INTO todo (note) VALUES ('(잠금화면)일정이 없을때는 우측상단 새로고침 버튼을 눌러보세요')" );

        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (1,'5분동안 스트레칭하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (1,'스쿼트 20회 하기')" );

        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'면도날 갈아주기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'영양제 챙겨먹기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'신발 빨래하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'이번주에 볼 영화 예매하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'썸남/녀에게 놀이공원 가자고 연락하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'친구(동료)와 점심 메뉴로 라면 먹기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'길가에 쓰레기 줍기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'이메일 정리하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'네이버 비밀번호 변경하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'혼자 코인노래방 가서 인기차트 1위곡 부르기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'편의점 알바생에게 수고하라는 말 한마디 건네주기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (2,'이불 털고 오기')" );

        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (3,'피아노 건반 몇개인지 알아보기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (3,'대한민국 10대 대통령 이름 검색하기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (3,'사람이 곧 하늘이라는 동학의 기본 사상은 무엇인지 알아보기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (3,'지구의 반지름 알아보기')" );

        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (4,'45도 각도로 커피를 들고 재수없게 셀카찍기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (4,'만원으로 하루 버텨보기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (4,'똥싸고 화장지 3장으로 해결해보기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (4,'SNS에 지금화면 캡처해서 하기싫다고 올리기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (4,'아침 기지개 키면서 방귀 뀌기')" );

        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (6,'아침 안먹고 출근해서 점심 배터지게 먹어보기')" );
        db.execSQL( "INSERT INTO recommend (category_id, recommend_value) VALUES (6,'로또 천원만 사보기')" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS todo");
        db.execSQL("DROP TABLE IF EXISTS category_info");
        db.execSQL("DROP TABLE IF EXISTS recommend");
        onCreate(db);
    }
    /*** clear db*/
    public void clear_db(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS todo");
        db.execSQL("DROP TABLE IF EXISTS category_info");
        db.execSQL("DROP TABLE IF EXISTS recommend");
        onCreate(db);
    }
    /*** create recommend value*/
    public void create_recommend(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO recommend (category_id, recommend_value) VALUES (7,'"+note+"')";
        db.execSQL(sql);
        Log.d("TAG", sql);
        db.close();
    }
    /***  get todo all*/
    public List<TodoValue> get_Todo_All() {
        List<TodoValue> todoList = new ArrayList<TodoValue>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM todo ORDER BY created_at ASC";
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do {
                TodoValue tv = new TodoValue();
                tv.setId(c.getInt(c.getColumnIndex("id")));
                tv.setNote(c.getString(c.getColumnIndex("note")));
                tv.setStatus(c.getInt(c.getColumnIndex("status"))); //0:true, 1:false
                tv.setCreatedAt(c.getString(c.getColumnIndex("created_at")));

                todoList.add(tv);
            } while (c.moveToNext());
        } else {
            todoList = null;
        }
        c.close();
        db.close();
        return todoList;
    }
    /*** get single todo value by id*/
    public TodoValue get_Todo_ById(int todo_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM todo WHERE "
                + "id = " + todo_id;

        Cursor c = db.rawQuery(sql, null);
        if (c != null)
            c.moveToFirst();

        TodoValue tv = new TodoValue();
        tv.setId(c.getInt(c.getColumnIndex("id")));
        tv.setNote(c.getString(c.getColumnIndex("note")));
        tv.setStatus(c.getInt(c.getColumnIndex("status")));
        tv.setCreatedAt(c.getString(c.getColumnIndex("created_at")));
        c.close();
        db.close();
        return tv;
    }
    /*** get todo value recent added 가장 최근에 추가된 아이템 가져오기*/
    public TodoValue get_Todo_Recent(){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM todo ORDER BY id DESC";

        Cursor c = db.rawQuery(sql, null);
        if (c != null)
            c.moveToFirst();

        TodoValue tv = new TodoValue();
        tv.setId(c.getInt(c.getColumnIndex("id")));
        tv.setNote(c.getString(c.getColumnIndex("note")));
        tv.setStatus(c.getInt(c.getColumnIndex("status")));
        tv.setCreatedAt(c.getString(c.getColumnIndex("created_at")));
        c.close();
        db.close();
        return tv;
    }
    /*** get todo value by date*/
    public List<TodoValue> get_Todo_ByDate(String date){
        List<TodoValue> todoList = new ArrayList<TodoValue>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM todo WHERE "
                + "created_at LIKE '%" + date + "%'";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                TodoValue tv = new TodoValue();
                tv.setId(c.getInt(c.getColumnIndex("id")));
                tv.setNote(c.getString(c.getColumnIndex("note")));
                tv.setStatus(c.getInt(c.getColumnIndex("status"))); //0:true, 1:false
                tv.setCreatedAt(c.getString(c.getColumnIndex("created_at")));

                todoList.add(tv);
            }while(c.moveToNext());
        } else{
            todoList=null;
        }
        c.close();
        db.close();
        return todoList;
    }
    /*** get todo value by date and status*/
    public List<TodoValue> get_Todo_ByDateAndStatus(String date, int status){
        List<TodoValue> todoList = new ArrayList<TodoValue>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM todo WHERE status = " + status + " AND "
                + "created_at LIKE '%" + date + "%'";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                TodoValue tv = new TodoValue();
                tv.setId(c.getInt(c.getColumnIndex("id")));
                tv.setNote(c.getString(c.getColumnIndex("note")));
                tv.setStatus(c.getInt(c.getColumnIndex("status"))); //0:true, 1:false
                tv.setCreatedAt(c.getString(c.getColumnIndex("created_at")));

                todoList.add(tv);
            }while(c.moveToNext());
        } else{
            todoList=null;
        }
        c.close();
        db.close();
        return todoList;
    }
    /*** create todo value*/
    public void create_Todo(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO todo (note) VALUES ('"+note+"')";
        db.execSQL(sql);
        Log.d("TAG", sql);
        db.close();
    }
    /*** create todo value with date*/
    public void create_Todo_WithDate(String note, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO todo (note, created_at) VALUES ('"+note+"','"+date+"')";
        db.execSQL(sql);
        Log.d("TAG", sql);
        db.close();
    }
    /*** update todo value*/
    public void update_Todo(int id, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE todo SET note = '" + note + "' WHERE id = " + id;
        db.execSQL(sql);
        db.close();
    }
    /*** update todo status */
    public void update_Todo_Status(int id, int status){
        if(status != 0 && status != 1) //0:true(finish), 1:false(not finish)
            status = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE todo SET status = " + status + " WHERE id = " + id;
        db.execSQL(sql);
        db.close();
    }
    /*** delete todo value*/
    public void delete_Todo(int todo_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM todo WHERE id = " + todo_id;
        db.execSQL(sql);
        db.close();
    }
    /*** get single category*/
    public CategoryValue get_Category(int category_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM category_info WHERE "
                + "id = " + category_id;

        Cursor c = db.rawQuery(sql, null);
        if (c != null)
            c.moveToFirst();

        CategoryValue cv = new CategoryValue();
        cv.setId(c.getInt(c.getColumnIndex("id")));
        cv.setCategory_Info(c.getString(c.getColumnIndex("category_name")));
        cv.setStatus(c.getInt(c.getColumnIndex("status")));
        c.close();
        db.close();
        return cv;
    }
    /*** get all category*/
    public List<CategoryValue> get_Category_All(){
        List<CategoryValue> categoryList = new ArrayList<CategoryValue>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM category_info";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                CategoryValue cv = new CategoryValue();
                cv.setId(c.getInt(c.getColumnIndex("id")));
                cv.setCategory_Info(c.getString(c.getColumnIndex("category_name")));
                cv.setStatus(c.getInt(c.getColumnIndex("status")));

                categoryList.add(cv);
            }while(c.moveToNext());
        } else{
            categoryList = null;
        }
        c.close();
        db.close();
        return categoryList;
    }
    /*** get category by status */
    public List<CategoryValue> get_Category_ByStatus(int status){
        if(status != 0 && status != 1) //0:true(visible), 1:false(not visible)
            status = 0;
        List<CategoryValue> categoryList = new ArrayList<CategoryValue>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM category_info WHERE status = " + status;
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                CategoryValue cv = new CategoryValue();
                cv.setId(c.getInt(c.getColumnIndex("id")));
                cv.setCategory_Info(c.getString(c.getColumnIndex("category_name")));
                cv.setStatus(c.getInt(c.getColumnIndex("status")));

                categoryList.add(cv);
            }while(c.moveToNext());
        }else{
            categoryList = null;
        }
        c.close();
        db.close();
        return categoryList;
    }
    /*** update category status */
    public void update_Category_Status(int id, int status){
        if(status != 0 && status != 1) //0:true(visible), 1:false(not visible)
            status = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE category_info SET status = " + status + " WHERE id = " + id;
        db.execSQL(sql);
        db.close();
    }
    /*** get recommend all */
    public List<RecommendedList> get_Recommend_All(){
        List<RecommendedList> recommendedLists = new ArrayList<RecommendedList>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM recommend";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                RecommendedList rv = new RecommendedList();
                rv.setId(c.getInt(c.getColumnIndex("id")));
                rv.setCategory_id(c.getInt(c.getColumnIndex("category_id")));
                rv.setTodo(c.getString(c.getColumnIndex("recommend_value")));
                rv.setStatus(c.getInt(c.getColumnIndex("status")));

                recommendedLists.add(rv);
            }while(c.moveToNext());
        }else{
            recommendedLists = null;
        }
        c.close();
        db.close();
        return recommendedLists;
    }
    /*** get recommend by status */
    public List<RecommendedList> get_Recommend_ByStatus(int status){
        if(status != 0 && status != 1) //0:true(finish), 1:false(not finish)
            status = 1;
        List<RecommendedList> recommendedLists = new ArrayList<RecommendedList>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM recommend WHERE status = " + status;
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                RecommendedList rv = new RecommendedList();
                rv.setId(c.getInt(c.getColumnIndex("id")));
                rv.setCategory_id(c.getInt(c.getColumnIndex("category_id")));
                rv.setTodo(c.getString(c.getColumnIndex("recommend_value")));
                rv.setStatus(c.getInt(c.getColumnIndex("status")));

                recommendedLists.add(rv);
            }while(c.moveToNext());
        }else{
            recommendedLists = null;
        }
        c.close();
        db.close();
        return recommendedLists;
    }
    /*** get recommend random*/
    public List<RecommendedList> get_Recommend_Random(int row_amount){
        ArrayList<RecommendedList> recommendedLists = new ArrayList<RecommendedList>();
        ArrayList<RecommendedList> randomLists = new ArrayList<RecommendedList>();

        String sql = "SELECT * FROM recommend"
        +" WHERE status = 1";// status가 not finish인 row만 가져온다

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);

        if (c != null && c.getCount() != 0){
            c.moveToFirst();
            do{
                RecommendedList rv = new RecommendedList();
                rv.setId(c.getInt(c.getColumnIndex("id")));
                rv.setCategory_id(c.getInt(c.getColumnIndex("category_id")));
                rv.setTodo(c.getString(c.getColumnIndex("recommend_value")));
                rv.setStatus(c.getInt(c.getColumnIndex("status")));

                recommendedLists.add(rv);
            }while(c.moveToNext());

            Collections.shuffle(recommendedLists);
            for(int i = 0; i<row_amount; i++){
                randomLists.add(recommendedLists.get(i));
            }
        }else{
            randomLists = null;
        }
        c.close();
        db.close();
        return randomLists;
    }
    /*** update recommend status */
    public void update_Recommend_Status(int category_id, int status){
        if(status != 0 && status != 1) //0:true(finish), 1:false(not finish)
            status = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE recommend SET status = " + status + " WHERE category_id = " + category_id;
        db.execSQL(sql);
        db.close();
    }
}
