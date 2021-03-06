/*
 *                    GNU GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 ******************************************************************************/

package semiworld.org.logger.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import semiworld.org.logger.R;
import semiworld.org.logger.activities.SecondActivity;
import semiworld.org.logger.models.Note;

/**
 * Created on 07.06.2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        return new NoteHolder(view);
    }

    // Here we are binding data to card via holder
    @Override
    public void onBindViewHolder(final NoteHolder holder, final int position) {

        final Note note = noteList.get(holder.getAdapterPosition());

        Date date = note.Date;
        String day = new SimpleDateFormat("d", Locale.getDefault()).format(date);
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
        final String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);

        holder._day.setText(day);
        holder._month.setText(month);
        holder._hour.setText(hour);
        holder._text.setText(note.Text);

        final Long _id = note.getId();

        // ========================================================================== //

        holder._textLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (holder._swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                    holder._swipeLayout.close();
                } else {
                    Intent intent = new Intent(v.getContext(), SecondActivity.class);
                    intent.putExtra("_id", _id);
                    v.getContext().startActivity(intent);
                }
            }
        });


        // This is where data removed then recycle view updated
        holder._btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new Delete().from(Note.class).where("id=?", _id).execute();
                noteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, noteList.size());
            }
        });


        // ========================================================================== //
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    // Here we have holder that kepting ui component
    // It is not compatible to use ButterKnife here yet
    class NoteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtDay) TextView _day;
        @BindView(R.id.txtMonth) TextView _month;
        @BindView(R.id.txtHour) TextView _hour;
        @BindView(R.id.txtText) TextView _text;

        // Added
        @BindView(R.id.swipe) SwipeLayout _swipeLayout;
        @BindView(R.id.btnDelete) Button _btnDelete;
        @BindView(R.id.container_rel_lay) RelativeLayout _textLayout;

        NoteHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
