package com.tim.test01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tim.test01.db.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText etId;
    private EditText etName;
    private EditText etGrade;
    private EditText etTime;

    private Button btnInsert;
    private Button btnSelect;
    private Button btnSelectById;
    private Button btnUpdateById;
    private Button btnDeleteById;
    private Button btnTransaction;


    private RecyclerView rvDatas;
    private ScoreRecycleViewAdapter adapter;

    private static final String DB_FILE_NAME = "score.db";
    MySQLiteOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setViewsListeners();
        initRecycleView();

        helper = new MySQLiteOpenHelper(this, DB_FILE_NAME,null,1);
        db=helper.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
        db = null;
        helper.close();
        helper=null;
    }

    private void findViews() {
        etId = findViewById(R.id.et_id);
        etName = findViewById(R.id.et_name);
        etGrade = findViewById(R.id.et_grade);
        etTime = findViewById(R.id.et_time);

        btnInsert = findViewById(R.id.btn_insert);
        btnSelect = findViewById(R.id.btn_select);
        btnSelectById = findViewById(R.id.btn_select_by_id);
        btnUpdateById = findViewById(R.id.btn_update_by_id);
        btnDeleteById = findViewById(R.id.btn_delete_by_id);
        btnTransaction = findViewById(R.id.btn_transaction);

        rvDatas = findViewById(R.id.rv_datas);
    }

    private void setViewsListeners() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String grade = etGrade.getText().toString();
                String time = etTime.getText().toString();
                String table = "score";
                String nullColumnHack = "name";
                ContentValues values = new ContentValues();
//                values.put("id", id);
                values.put("name", name);
                values.put("grade", grade);
                values.put("time", time);

                try {
                    long rowId = db.insertOrThrow(table, nullColumnHack, values);
                    Log.i(TAG, "insert: rowId = " + rowId);
                } catch (SQLException e) {
                    Log.e(TAG, "insert: ", e);
                }
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String table = "score";
                String[] columns = {"id", "name", "grade", "time"};
                String selection = null;
                String[] selectionArgs = null;
                String groupBy = null;
                String having = null;
                String limit = null;
                Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, limit);

                List<Map<String, Object>> scores = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String grade = cursor.getString(cursor.getColumnIndex("grade"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));

                    Map<String, Object> score = new HashMap<>();
                    score.put("id", id);
                    score.put("name", name);
                    score.put("grade", grade);
                    score.put("time", time);

                    scores.add(score);
                }
                Log.i(TAG, "select:employees=" + scores);

                adapter.setScores(scores);
                adapter.notifyDataSetChanged();
            }
        });
        btnSelectById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer argId = Integer.valueOf(etId.getText().toString());
                String table = "score";
                String[] columns = {"id", "name", "grade", "time"};
                String selection = "id=?";
                String[] selectionArgs = {String.valueOf(argId)};
                String groupBy = null;String having = null;
                String limit = null;
                Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, limit);
                List<Map<String, Object>> scores = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String grade = cursor.getString(cursor.getColumnIndex("grade"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    Map<String, Object> score = new HashMap<>();
                    score.put("id", id);score.put("name", name);
                    score.put("grade", grade);score.put("time", time);
                    scores.add(score);
                }
                Log.i(TAG, "select:scores=" + scores);
                adapter.setScores(scores);adapter.notifyDataSetChanged();
            }
        });
        btnUpdateById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = Integer.valueOf(etId.getText().toString()); // 建议使用try-catch做异常捕获
                String name = etName.getText().toString();
                String grade = etGrade.getText().toString();
                String time = etTime.getText().toString();

                String table = "score";
                ContentValues values = new ContentValues();
//                values.put("id", id);
                values.put("name", name);
                values.put("grade", grade);
                values.put("time", time);

                String whereClause = "id=?";
                String[] whereArgs = {String.valueOf(id)};
                int rowOfAffected = db.update(table, values, whereClause, whereArgs);
                Log.i(TAG, "update by id:rowOfAffected=" + rowOfAffected);
            }
        });
        btnDeleteById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = Integer.valueOf(etId.getText().toString());

                String table = "score";
                String whereClause = "id=?";
                String[] whereArgs = {String.valueOf(id)};
                int rowOfAffected = db.delete(table, whereClause, whereArgs);
                Log.i(TAG, "delete by id:rowOfAffected=" + rowOfAffected);
            }
        });

        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = Integer.valueOf(etId.getText().toString());
                String name = etName.getText().toString();
                String grade = etGrade.getText().toString();
                String time = etTime.getText().toString();
                String table = "score";
                String nullColumnHack = "name";
                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("name", name);
                values.put("grade", grade);
                values.put("time", time);
                List<Long> rowIds = new ArrayList<>();
                db.beginTransaction();//开始事务
                try {
                    for (int i = 0; i < 2; i++) {
                        long rowId = db.insertOrThrow(table, nullColumnHack, values);
                        Log.i(TAG, "insert(Transaction): rowId = " + rowId);
                        rowIds.add(rowId);
                    }
                    db.setTransactionSuccessful();
                    Log.e(TAG, "insert(Transaction):commited ");
                } catch (SQLException e) {
                    Log.e(TAG, "insert(Transaction): failed，be rolled back", e);
                } finally {
                    db.endTransaction();
                }
                Log.i(TAG,"transaction-insert:rowIds="+rowIds);
            }
        });
    }

    private void initRecycleView() {
        rvDatas.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDatas.addItemDecoration(dividerItemDecoration);

        adapter = new ScoreRecycleViewAdapter();
        adapter.setOnItemClickListener(new ScoreRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScoreRecycleViewAdapter.ScoreViewHolder holder, int position, Map<String, Object> score) {
//                View itemView=holder.itemView;
//                holder.tvName;

                if (null == score) {
                    return;
                }

                etId.setText(Objects.toString(score.get("id")));
                etName.setText(Objects.toString(score.get("name")));
                etGrade.setText(Objects.toString(score.get("grade")));
                etTime.setText(Objects.toString(score.get("time")));
            }
        });
        rvDatas.setAdapter(adapter);
    }
    static class ScoreRecycleViewAdapter extends RecyclerView.Adapter<ScoreRecycleViewAdapter.ScoreViewHolder> {

        public static class ScoreViewHolder extends RecyclerView.ViewHolder {
            private TextView tvId;
            private TextView tvName;
            private TextView tvGrade;
            private TextView tvTime;
            ;

            public ScoreViewHolder(@NonNull View itemView) {
                super(itemView);
                if (null == itemView) {
                    return;
                }
                tvId = itemView.findViewById(R.id.tv_id);
                tvName = itemView.findViewById(R.id.tv_name);
                tvGrade = itemView.findViewById(R.id.tv_grade);
                tvTime = itemView.findViewById(R.id.tv_time);

            }
        }

        interface OnItemClickListener {
            void onItemClick(ScoreViewHolder holder, int position, Map<String, Object> score);
        }

        private List<Map<String, Object>> scores;
        private ScoreRecycleViewAdapter.OnItemClickListener onItemClickListener;

        @NonNull
        @Override
        public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ll_adapter_item_score, parent, false);
            ScoreViewHolder holder = new ScoreViewHolder(v);
            return holder;
        }

        public List<Map<String, Object>> getScores() {
            return scores;
        }

        public void setScores(List<Map<String, Object>> scores) {
            this.scores = scores;
        }

        public OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onBindViewHolder(@NonNull final ScoreViewHolder holder, final int position) {
            if (null == this.scores) {
                if (null != this.onItemClickListener) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(holder, position, null);
                        }
                    });
                }
                return;
            }
            if (position < 0 || position >= this.scores.size()) {
                if (null != this.onItemClickListener) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(holder, position, null);
                        }
                    });
                }
                return;
            }
            final Map<String, Object> score = this.scores.get(position);
            if (null == score) {
                if (null != this.onItemClickListener) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(holder, position, null);
                        }
                    });
                }
                return;
            }

            if (null != this.onItemClickListener) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder, position, score);
                    }
                });
            }

            holder.tvId.setText(Objects.toString(score.get("id")));
            holder.tvName.setText(Objects.toString(score.get("name")));
            holder.tvGrade.setText(Objects.toString(score.get("grade")));
            holder.tvTime.setText(Objects.toString(score.get("time")));
        }

        @Override
        public int getItemCount() {
            return null == this.scores ? 0 : this.scores.size();
        }
    }
}