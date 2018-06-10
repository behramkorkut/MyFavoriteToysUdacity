package com.yenimobile.myfavoritetoysudacity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yenimobile.myfavoritetoysudacity.R;
import com.yenimobile.myfavoritetoysudacity.utilities.ColorUtils;

public class SomeAdapter extends RecyclerView.Adapter<SomeAdapter.SomeViewHolder> {

    private static final String LOGTAG = SomeAdapter.class.getSimpleName();
    private int mCountNumber;

    final private SomeClickInterface mSomeClickInterface;

    public interface SomeClickInterface {
        void onListItemClick(int index);
    }

    public SomeAdapter(int countNumber, SomeClickInterface listener){
        mCountNumber = countNumber;
        mSomeClickInterface = listener;
    }

    @Override
    public SomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.number_list_item, parent, false);
        SomeViewHolder someViewHolder = new SomeViewHolder(view);
        return someViewHolder;
    }

    @Override
    public void onBindViewHolder(SomeViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setBackgroundColor(
                ColorUtils.makeTheRecyclerViewAlternateColors(holder.itemView.getContext(), position)
        );
    }


    @Override
    public int getItemCount() {
        return mCountNumber;
    }



    //---------------------------------View Holder Here--------------------------------------------------
    class SomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView someTv;

        public SomeViewHolder(View itemView) {
            super(itemView);

            someTv = itemView.findViewById(R.id.tv_item_number);
            itemView.setOnClickListener(this);

        }

        public void bind(int index){
            someTv.setText(String.valueOf(index));
            //itemView.setBackgroundColor(ColorUtils.makeTheRecyclerViewAlternateColors(itemView.getContext(), index));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mSomeClickInterface.onListItemClick(clickedPosition);
        }
    }
    //--------------------------------------------------------------------------------------------------
}
