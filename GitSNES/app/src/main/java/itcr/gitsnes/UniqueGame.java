package itcr.gitsnes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JuanPC on 26/10/2014.
 */
public class UniqueGame extends Fragment{

    String name,category,desc;
    
    
    public UniqueGame(String name, String category, String desc){
        this.name = name;
        this.category = category;
        this.desc=desc;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unique_game, container, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.Uni_name);
        nameTextView.setText(this.name);
        TextView typeView = (TextView) view.findViewById(R.id.Uni_category);
        typeView.setText(this.category);
        TextView descView = (TextView) view.findViewById(R.id.Uni_decription);
        descView.setText(this.desc);
        return view;
    }


}
