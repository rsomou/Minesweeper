package com.example.project_1;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;

    private ArrayList<int[]> directions = new ArrayList<>(Arrays.asList(
            new int[]{-1, -1},  // top-left
            new int[]{-1, 0},   // top
            new int[]{-1, 1},   // top-right
            new int[]{0, -1},   // left
            new int[]{0, 1},    // right
            new int[]{1, -1},   // bottom-left
            new int[]{1, 0},    // bottom
            new int[]{1, 1}     // bottom-right
    ));


    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    private ArrayList<Integer> game_state;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void initialize_game_state() {
        game_state = new ArrayList<Integer>();
        // Fill the game_state with zeroes
        for (int i = 0; i < ROW_COUNT * COLUMN_COUNT; i++) {
            game_state.add(0);
        }

        // Randomly place 4 ones
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index;
            do {
                index = random.nextInt(ROW_COUNT * COLUMN_COUNT);
            } while (game_state.get(index) == -1);  // Ensure we don't overwrite an existing 1

            game_state.set(index, -1);

            int r = index/COLUMN_COUNT;
            int c = index%COLUMN_COUNT;
            for(int[] direction:directions){
                int[] neighbor = {r+direction[0],c+direction[1]};
                if(neighbor[0]>=0 && neighbor[0]<ROW_COUNT && neighbor[1]>=0 && neighbor[1]<COLUMN_COUNT){
                    if(game_state.get(neighbor[0]*COLUMN_COUNT+neighbor[1])!=-1){
                        game_state.set(neighbor[0]*COLUMN_COUNT+neighbor[1], game_state.get(neighbor[0]*COLUMN_COUNT+neighbor[1])+1);
                    }
                }

            }

        }
    }

    private void bfs(int start_idx){
        Queue<Integer> q = new LinkedList<Integer>();
        q.offer(start_idx);
        TextView tv = cell_tvs.get(start_idx);
        Drawable background = tv.getBackground();
        if (background instanceof ColorDrawable &&
                ((ColorDrawable) background).getColor() == Color.GREEN) {
            tv.setBackgroundColor(Color.LTGRAY);
        }

        while(!q.isEmpty()){
            int state = q.poll();

            int r = state/COLUMN_COUNT;
            int c = state%COLUMN_COUNT;
            for(int[] direction:directions){
                int[] neighbor = {r+direction[0],c+direction[1]};
                if(neighbor[0]>=0 && neighbor[0]<ROW_COUNT && neighbor[1]>=0 && neighbor[1]<COLUMN_COUNT){
                    int n_idx = neighbor[0]*COLUMN_COUNT+neighbor[1];
                    TextView tmp_tv = cell_tvs.get(n_idx);
                    Drawable tmp_background = tmp_tv.getBackground();
                    if (((ColorDrawable) tmp_background).getColor() == Color.GREEN && game_state.get(n_idx)==0) {
                        tmp_tv.setBackgroundColor(Color.LTGRAY);
                        q.offer(neighbor[0]*COLUMN_COUNT+neighbor[1]);
                    }
                    if(((ColorDrawable) tmp_background).getColor() == Color.GREEN && game_state.get(n_idx)>0){
                        tmp_tv.setText(String.valueOf(game_state.get(n_idx)));
                        tmp_tv.setTextColor(Color.GRAY);
                        tmp_tv.setBackgroundColor(Color.LTGRAY);
                    }
                }

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();

        initialize_game_state();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i<ROW_COUNT; i++) {
            for (int j=0; j<COLUMN_COUNT; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(28) );
                tv.setWidth( dpToPixel(28) );
                tv.setTextSize( dpToPixel(8) );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(1), dpToPixel(1), dpToPixel(1), dpToPixel(1));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
    }



    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        if(game_state.get(n) == -1){
            tv.setText(getString(R.string.mine));

            Drawable background = tv.getBackground();
            if (background instanceof ColorDrawable &&
                    ((ColorDrawable) background).getColor() == Color.GREEN) {
                tv.setBackgroundColor(Color.LTGRAY);
            }

            //some intent transistion to new pag (lose)
        } else {
            if(game_state.get(n) == 0){
                bfs(n);
                return;
            }

            tv.setText(String.valueOf(game_state.get(n)));

            Drawable background = tv.getBackground();
            if (((ColorDrawable) background).getColor() == Color.GREEN) {
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }


        }


    }
}