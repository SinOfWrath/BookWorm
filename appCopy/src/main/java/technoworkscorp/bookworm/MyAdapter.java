package technoworkscorp.bookworm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.Html;

/**
 * Created by nitinbudhwar on 15/08/15.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<Book> books;
        private Context mContext;
        //Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
           // protected ImageView imageView;
           protected TextView textView;

            public ViewHolder(View v) {
                super(v);
                //this.imageView = (ImageView) v.findViewById(R.id.cover_page);
                this.textView = (TextView) v.findViewById(R.id.book_name);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Context context,List<Book> mybooks) {
            this.mContext= context;
            this.books = mybooks;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new ViewHolder(v);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) view.getTag();
                int position = holder.getPosition();

                Book book = books.get(position);
                Toast.makeText(mContext, book.getBook_name(), Toast.LENGTH_SHORT).show();
            }
        };

    // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Book book = books.get(position);

            //DOWNLOAD THE IMAGE

            holder.textView.setText(book.getBook_name());
            holder.textView.setOnClickListener(clickListener);
            //ViewHolder.imageView.setOnClickListener(clickListener);
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return (null != books ? books.size() : 0);

        }

}
