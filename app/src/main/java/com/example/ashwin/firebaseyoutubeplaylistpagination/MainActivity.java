package com.example.ashwin.firebaseyoutubeplaylistpagination;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements VideosAdapter.VideoListener
{
    private RecyclerView mRecyclerView;
    private VideosAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Video> mVideosList;
    private int mLimit=5, mStartIndex=1, mEndIndex=5;
    private DatabaseReference mReadRef;
    private Query mQueryRef;
    private int mPosition = 0;
    private CircleProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (CircleProgressBar) findViewById(R.id.circleProgressBar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);

        mVideosList = new ArrayList<Video>();

        mReadRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://android-firebase-test-a2516.firebaseio.com/youtube-playlist/videos/");

        mAdapter = new VideosAdapter(this, mVideosList);

        updateQuery();

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateQuery()
    {
        Log.i("start index", String.valueOf(mStartIndex));
        mQueryRef = mReadRef.orderByChild("position").startAt(mStartIndex).limitToFirst(mLimit);
    }

    private void initRecyclerView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mQueryRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Video video = dataSnapshot.getValue(Video.class);

                Log.i("video string", video.toString());

                //Adding video to videos list
                mVideosList.add(video);
                Log.i("videos list size", String.valueOf(mVideosList.size()));

                mAdapter.notifyDataSetChanged();


                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        Log.i("videos list size", String.valueOf(mVideosList.size()));
    }

    @Override
    public void loadMoreVideos()
    {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        if(firstVisiblePosition >= mEndIndex-mLimit)
        {
            mStartIndex = mEndIndex+1;
            mEndIndex += mLimit;
            updateQuery();
            mProgressBar.setVisibility(View.VISIBLE);
            updateRecyclerView();
        }
    }

    private void updateRecyclerView()
    {
        mQueryRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                for (DataSnapshot postSnapshot : snapshot.getChildren())
                {
                    //Getting the data from snapshot
                    Video video = postSnapshot.getValue(Video.class);

                    //Log.i("video string", video.toString());

                    //Adding video to videos list
                    mVideosList.add(video);
                    //Log.i("videos list size", String.valueOf(mVideosList.size()));

                    mAdapter.notifyDataSetChanged();

                }

                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onVideoImageClicked(View view, int position) {
        if(mVideosList.get(position).getType().equals("video"))
        {
            try
            {
                mPosition = position;
                Toast.makeText(MainActivity.this, "Now playing : " + mVideosList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(getApplicationContext(), mVideosList.get(position).getId(), true, false);
                startActivity(intent);
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "Youtube is not installed", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //do nothing
        }
    }
}
