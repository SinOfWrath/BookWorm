package technoworkscorp.bookworm;

import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.ProgressBar;
import android.view.View;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import android.os.Message;
import android.util.JsonToken;

public class MainActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private static final String TAG = "BookWorm";
    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect to the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);        //use true if the size of cards remains fixed

        mAdapter = new MyAdapter(MainActivity.this, books);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Connect to Database
        final String url = "http://bookworm.tweetent.com/books/_design/useful/_view/by_courseid";
        new AsyncHttpTask().execute(url);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

    }
        public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

            @Override
            protected void onPreExecute() {
                setProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected Integer doInBackground(String... params) {
                Integer result = 0;
                HttpURLConnection urlConnection;

                Log.v(TAG,"nitin1");

                try {
                    URL url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    int statusCode = urlConnection.getResponseCode();

                    // 200 represents HTTP OK

                    if (statusCode == 200) {
                        JsonReader reader = new JsonReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                        //try{
                            books = parseResult(reader);

                            Log.v(TAG,"LOL");
                        //}
                        //finally {
                            reader.close();
                       // }
                        result = 1; // Successful

                    } else {
                        result = 0; //"Failed to fetch data!";
                    }
                } catch (Exception e) {
                    Log.d(TAG,"LOL2");
                }
                return result; //"Failed to fetch data!";
            }

            @Override
            protected void onPostExecute(Integer result) {
                // Download complete. Let us update UI
                progressBar.setVisibility(View.GONE);
               // if (result == 1) {
                   mAdapter = new MyAdapter(getApplicationContext(), books);
                    mRecyclerView.setAdapter(mAdapter);

                //} else {
                  //  Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                //}
            }
        }

        public List<Book> parseResult(JsonReader reader) throws IOException {
            long id = -1;
            String book_name = null;
            String course_id = null;
            //String course_name = null;


            //Book book = new Book();
            reader.beginObject();

            while (reader.hasNext()) {
                Log.v(TAG, "LOL3");

                String name = reader.nextName();

                //Log.v("BookName", name);
                if(name.equals("rows") && reader.peek() != JsonToken.NULL) {
                    books = readBooksArray(reader);
                }
//                else if (name.equals("id")) {
//                    id = reader.nextLong();
//                    book.setid(id);
//
//                } else if (name.equals("key")) {
//                    course_id = reader.nextString();
//                    book.setCourse_id(course_id);
//                    Log.v("Course_id",course_id);
//                }
//                else if (name.equals("value")) {
//                    book_name = readvalue(reader);
//                    book.setBook_name(book_name);
//                    books.add(book);
//                    book = new Book();
                 //}
                 else {
                    reader.skipValue();
                }

            }
            reader.endObject();
            return books;
        }

    public List<Book> readBooksArray(JsonReader reader) throws IOException {
        List<Book> books = new ArrayList<Book>();
        List doubles = new ArrayList();

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();

            if(name.equals("rows") && reader.peek() != JsonToken.NULL) {
                books = readBooksArray(reader);
            }
//                else if (name.equals("id")) {
//                    id = reader.nextLong();
//                    book.setid(id);
//
//                } else if (name.equals("key")) {
//                    course_id = reader.nextString();
//                    book.setCourse_id(course_id);
//                    Log.v("Course_id",course_id);
//                }
//                else if (name.equals("value")) {
//                    book_name = readvalue(reader);
//                    book.setBook_name(book_name);
//                    books.add(book);
//                    book = new Book();
            //}
            else {
                reader.skipValue();
            }
        }

        reader.endArray();
        return doubles;
    }

    public String readvalue(JsonReader reader) throws IOException {
        String book_name = null;
      //  List link = null;
        //int followersCount = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                book_name = reader.nextString();
                reader.endObject();
                return book_name;
            }
//            else if (name.equals("link") && reader.peek() != JsonToken.NULL) {
//                link = readDoublesArray(reader);
//            }
            //else if (name.equals("followers_count")) {
                //followersCount = reader.nextInt();}
             else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return book_name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}