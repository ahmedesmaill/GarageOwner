/*#if(${PACKAGE_NAME}&&${PACKAGE_NAME}!="")package ${PACKAGE_NAME}; #end

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ${Adapter} extends RecyclerView.Adapter<${Adapter}.${ViewHolder}> {
    ArrayList<${Model_Name}> models=new ArrayList<>();

public void setModels(ArrayList<${Model_Name}> models) {
        this.models = models;
        notifyDataSetChanged();
        }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.${Layout},parent,false);
        return new ${ViewHolder}(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ${ViewHolder} holder, int position) {
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ${ViewHolder} extends RecyclerView.ViewHolder
    {

        public ${ViewHolder}(@NonNull View itemView) {
            super(itemView);
        }
    }
}
*/