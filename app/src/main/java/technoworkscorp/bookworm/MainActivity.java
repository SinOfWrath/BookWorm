package technoworkscorp.bookworm;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.app.SearchManager;
import android.support.v4.view.MenuItemCompat;
import android.content.Intent;
import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import android.widget.Toast;
import java.net.HttpURLConnection;
import android.content.Context;
import android.widget.ProgressBar;
import android.view.View;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private MyBookAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private static final String TAG = "BookWorm";
    private final String url = "http://bookworm.tweetent.com/books/_design/useful/_view/by_courseid";

    private List<Book> bookList;
    private List<Book> cloneBookList;
    String buy_link = null;
    String download_link = null;
    JSONArray books = null;
    JSONArray links = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect to the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);                //use true if the size of cards remains fixed
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Connect to Database and fetch bookList
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

            try {
                URL urlconn = new URL(params[0]);
                urlConnection = (HttpURLConnection) urlconn.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {

                    ServiceHandler sh = new ServiceHandler();

                    // Making a request to url and getting response
                    String json = sh.makeServiceCall(params[0], ServiceHandler.GET);

                    bookList = new ArrayList<Book>();

                    if (json != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(json);

                            // Getting JSON Array node
                            books = jsonObj.getJSONArray("rows");

                            // looping through All Books
                            for (int i = 0; i < books.length(); i++) {

                                JSONObject c = books.getJSONObject(i);

                                String id = c.getString("id");
                                String key = c.getString("key");


                                // value node is JSON Object
                                JSONObject value = c.getJSONObject("value");
                                String imgurl = value.getString("image");
                                String book_name = value.getString("name");

                                //  JSON array for download and buy links
                                links = value.getJSONArray("link");
                                for(int j = 0; j < links.length(); j++){

                                    JSONObject blink = links.getJSONObject(j);

                                    String type = blink.getString("type");
                                            if(type.equals("buy")){
                                                buy_link = blink.getString("origin");
                                            }
                                            else if(type.equals("download")){
                                                download_link = blink.getString("origin");
                                            }
                                }

                                //details of course
                                JSONObject course = value.getJSONObject("course");
                                String course_name = course.getString("name");

                                // Single book
                                Book book = new Book();

                                book.setid(id);
                                book.setThumbnail(imgurl);
                                book.setCourse_id(key);
                                book.setBook_name(book_name);
                                book.setCourse_name(course_name);
                                book.setBuyLink(buy_link);
                                book.setDownload_link(download_link);

                                // adding book to  BookList
                                bookList.add(book);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                    result = 1; // Successful
                    getCloneBookList();
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG,"Something Happened");
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                mAdapter = new MyBookAdapter(getApplicationContext(), bookList);
                mRecyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        try {
            final MenuItem item = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setQueryHint("Book name,Course id or name");
            searchView.setOnQueryTextListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement our filter logic
        if(cloneBookList.size() == 0 && query.equals("")){
            getCloneBookList();
        }

        query = query.toLowerCase();
        final List<Book> filteredBookList = filter(cloneBookList, query);
        mAdapter.animateTo(filteredBookList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    private List<Book> filter(List<Book> books, String query) {

        final List<Book> filteredBookList = new ArrayList<>();
        for (Book book : books) {
            final String book_name = book.getBook_name().toLowerCase();
            final String course_name = book.getCourse_name().toLowerCase();
            final String course_id  = book.getCourse_id().toLowerCase();
            if (book_name.contains(query.toLowerCase()) || course_id.contains(query.toLowerCase())
                    || course_name.contains(query.toLowerCase()) )
            {
                filteredBookList.add(book);
            }
        }
        return filteredBookList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            Toast.makeText(getApplicationContext(),"No Updates Available", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_refresh){

            new AsyncHttpTask().execute(url);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // clone bookList into new  List<Book>
    public List<Book> getCloneBookList() {
        cloneBookList = new ArrayList<Book>(bookList);
        return cloneBookList;
    }
}