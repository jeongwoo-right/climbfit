package com.scsa.myproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.myproject.databinding.RowBinding;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParserMainActivity extends AppCompatActivity {

    private static final String TAG = "ParserMainActivity_SCSA";

    ListView listView;
    MyAdapter adapter;
    List<Check> list = new ArrayList<>();

    EditText keywordEdit;
    Button searchButton;
    Button btnSports, btnHealth;
    TextView sectionTitle;

    String keyword = "";
    String currentRssUrl = "https://www.hani.co.kr/rss/sports/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser_main);

        listView = findViewById(R.id.result);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        keywordEdit = findViewById(R.id.keywordEdit);
        searchButton = findViewById(R.id.searchButton);
        btnSports = findViewById(R.id.btnSports);
        btnHealth = findViewById(R.id.btnHealth);
        sectionTitle = findViewById(R.id.sectionTitle);

        // 초기 상태
        updateSegmentedUI(true);
        sectionTitle.setText("Sports News");
        new MyAsyncTask().execute(currentRssUrl);

        btnSports.setOnClickListener(v -> {
            currentRssUrl = "https://www.hani.co.kr/rss/sports/";
            keyword = "";
            keywordEdit.setText("");
            list.clear();
            updateSegmentedUI(true);
            sectionTitle.setText("Sports News");
            new MyAsyncTask().execute(currentRssUrl);
        });

        btnHealth.setOnClickListener(v -> {
            currentRssUrl = "https://rss.donga.com/health.xml";
            keyword = "";
            keywordEdit.setText("");
            list.clear();
            updateSegmentedUI(false);
            sectionTitle.setText("Health News");
            new MyAsyncTask().execute(currentRssUrl);
        });

        searchButton.setOnClickListener(v -> {
            keyword = keywordEdit.getText().toString().trim();
            list.clear();
            new MyAsyncTask().execute(currentRssUrl);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = list.get(position).link;
            Intent intent = new Intent(ParserMainActivity.this, WebViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        });
    }

    private void updateSegmentedUI(boolean isSportsSelected) {
        if (isSportsSelected) {
            btnSports.setBackgroundResource(R.drawable.segmented_selected);
            btnSports.setTextColor(Color.WHITE);
            btnHealth.setBackgroundResource(R.drawable.segmented_unselected);
            btnHealth.setTextColor(Color.BLACK);
        } else {
            btnHealth.setBackgroundResource(R.drawable.segmented_selected);
            btnHealth.setTextColor(Color.WHITE);
            btnSports.setBackgroundResource(R.drawable.segmented_unselected);
            btnSports.setTextColor(Color.BLACK);
        }
    }

    class MyAsyncTask extends AsyncTask<String, String, List<Check>> {
        @Override
        protected List<Check> doInBackground(String... arg) {
            try {
                Log.d(TAG, "connection start....");
                InputStream input = new URL(arg[0]).openConnection().getInputStream();
                Log.d(TAG, "connection ok....");
                parsing(new BufferedReader(new InputStreamReader(input)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        protected void onPostExecute(List<Check> result) {
            Log.d(TAG, "onPostExecute: " + list.size());
            adapter.notifyDataSetChanged();
        }

        XmlPullParser parser = Xml.newPullParser();

        private void parsing(Reader reader) throws Exception {
            parser.setInput(reader);
            int eventType = parser.getEventType();
            Check item = null;
            long id = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("item")) {
                            item = new Check();
                            item.id = ++id;
                        } else if (item != null) {
                            if (name.equalsIgnoreCase("title")) {
                                item.title = parser.nextText();
                            } else if (name.equalsIgnoreCase("link")) {
                                item.link = parser.nextText();
                            } else if (name.equalsIgnoreCase("description")) {
                                item.description = parser.nextText();
                            } else if (name.equalsIgnoreCase("pubDate")) {
                                item.pubDate = new Date();  // 간단히 처리
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("item") && item != null) {
                            if (keyword.isEmpty()
                                    || (item.title != null && item.title.contains(keyword))
                                    || (item.description != null && item.description.contains(keyword))) {
                                list.add(item);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                RowBinding binding = RowBinding.inflate(LayoutInflater.from(ParserMainActivity.this), viewGroup, false);
                convertView = binding.getRoot();
                holder = new ViewHolder();
                holder.title = binding.title;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Check item = list.get(position);
            holder.title.setText(item.title);
            return convertView;
        }

        class ViewHolder {
            TextView title;
        }

        @Override
        public int getCount() { return list.size(); }

        @Override
        public Object getItem(int i) { return list.get(i); }

        @Override
        public long getItemId(int i) { return list.get(i).id; }
    }
}
