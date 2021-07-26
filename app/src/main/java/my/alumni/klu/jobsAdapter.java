package my.alumni.klu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.alumni.klu.Model.jobs;

public class jobsAdapter extends RecyclerView.Adapter<jobsAdapter.ArtistViewHolder> implements Filterable {
    private Context mCtx;
    private List<jobs> artistList;
    private List<jobs> artistsnew;
    private DatabaseReference mDatabaseRef,abc;
    public jobsAdapter(Context mCtx, List<jobs> artistList) {
        this.mCtx = mCtx;
        this.artistList = artistList;

    }
    void artist1234(List<jobs> artistList){
        artistsnew=new ArrayList<>(artistList);
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.jobs_artists, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        jobs artist = artistList.get(position);
        holder.textViewName.setText(artist.name);
        holder.jobtype.setText("Job-type: "+artist.jobs);
        holder.textViewGenre.setText("Date: " + artist.date);
        holder.salary.setText("Salary: "+artist.salary);
        holder.textViewAge.setText(artist.link);
        holder.textViewCountry.setText("Description: " + artist.des);
    }





    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public int getItemNo(){
        try{  return  artistsnew.size();}catch (Exception e){
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<jobs> filteredList=new ArrayList<>();

            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(artistsnew);
            }
            else {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (jobs item: artistsnew){
                    if(item.getName().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            artistList.clear();
            artistList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    static String flag="";
    public String deleteItem(int position,String s1){
        jobs selectedItem = artistList.get(position);
        final String selectedKey = selectedItem.getId();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Jobs");
        abc = FirebaseDatabase.getInstance().getReference("Jobs").child(selectedKey).child("user");
        abc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s="";
                try{s=dataSnapshot.getValue().toString();}catch (Exception e){}
                if(s.equals(s1))
                {  mDatabaseRef.child(selectedKey).removeValue();
                    flag="true";}else{
                    flag="false";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return flag;


    }



    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewGenre, textViewAge, textViewCountry,jobtype,salary;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
jobtype=itemView.findViewById(R.id.jobsType);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewGenre = itemView.findViewById(R.id.text_view_genre);
            salary=itemView.findViewById(R.id.salary);
            textViewAge = itemView.findViewById(R.id.text_view_age);
            textViewAge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse(textViewAge.getText().toString());
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    mCtx.startActivity(launchBrowser);
                }
            });
            textViewCountry = itemView.findViewById(R.id.text_view_country);

        }

    }


}

