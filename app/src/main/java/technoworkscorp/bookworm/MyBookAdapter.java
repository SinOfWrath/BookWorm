package technoworkscorp.bookworm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.InputStream;
import android.os.AsyncTask;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by nitinbudhwar on 15/08/15.
 */

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder> {

        private List<Book> books;
        private Context mContext;

        //Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            protected ImageView imageView;
            protected TextView book_name;
            protected TextView course_id;
            protected TextView course_name;
            protected Button buy;
            protected Button download;

            public ViewHolder(View v) {
                super(v);
                this.imageView = (ImageView) v.findViewById(R.id.cover_page);
                this.book_name = (TextView) v.findViewById(R.id.book_name);
                this.course_id = (TextView) v.findViewById(R.id.course_id);
                this.buy = (Button) v.findViewById(R.id.buy_link);
                this.download = (Button) v.findViewById(R.id.download_link);
                this.course_name = (TextView) v.findViewById(R.id.course_name);

            }
        }

    // Provide a suitable constructor (depends on the kind of dataset)
        public MyBookAdapter(Context context, List<Book> mybooks) {
            this.mContext= context;
            this.books = mybooks;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Book book = books.get(position);

            //DOWNLOAD THE IMAGE
            holder.book_name.setText(book.getBook_name());
            holder.course_id.setText(book.getCourse_id());
            holder.course_name.setText(book.getCourse_name());
            new DownloadImageTask(holder.imageView).execute(book.getThumbnail());
            holder.buy.setTag(book.getBuy_link());
            holder.download.setTag(book.getDownload_link());

            holder.book_name.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                   // Toast.makeText(mContext,"book_nameClickListener", Toast.LENGTH_SHORT).show();
                }
            });

            holder.course_id.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    //Toast.makeText(mContext,"CourseidClickListener", Toast.LENGTH_SHORT).show();
                }
            });

            holder.course_name.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    //Toast.makeText(mContext,"Course_nameClickListener", Toast.LENGTH_SHORT).show();
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

               //     Toast.makeText(mContext,"imageviewClickListener", Toast.LENGTH_SHORT).show();
                }
            });

            holder.download.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

                    //Toast.makeText(mContext, (String)view.getTag(),Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse((String)view.getTag());
                    Intent intent_dwnld = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        intent_dwnld.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent_dwnld);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            holder.buy.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                   // Toast.makeText(mContext, (String)view.getTag(), Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse((String)view.getTag());
                    Intent intent_buy = new Intent(Intent.ACTION_VIEW);
                    intent_buy.setData(uri);
                    try {
                        intent_buy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent_buy);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return books.size();

    }

    public void animateTo(List<Book> books) {
        applyAndAnimateRemovals(books);
        applyAndAnimateAdditions(books);
        applyAndAnimateMovedItems(books);
    }

    public Book removeItem(int position) {
        final Book book = books.remove(position);
        notifyItemRemoved(position);
        return book;
    }

    public void addItem(int position, Book book) {
        books.add(position, book);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Book book = books.remove(fromPosition);
        books.add(toPosition, book);
        notifyItemMoved(fromPosition, toPosition);
    }


    private void applyAndAnimateRemovals(List<Book> newModels) {
        for (int i = books.size() - 1; i >= 0; i--) {
            final Book book = books.get(i);
            if (!newModels.contains(book)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Book> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Book book = newModels.get(i);
            if (!books.contains(book)) {
                addItem(i, book);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Book> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Book book = newModels.get(toPosition);
            final int fromPosition = books.indexOf(book);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    //Class for downloading image using Url in the background
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String imgUrl = urls[0];
            Bitmap book_cover = null;
            try {
                InputStream in = new java.net.URL(imgUrl).openStream();
                book_cover = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return book_cover;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
