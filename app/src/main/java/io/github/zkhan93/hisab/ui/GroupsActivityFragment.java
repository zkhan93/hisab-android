package io.github.zkhan93.hisab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zkhan93.hisab.R;
import io.github.zkhan93.hisab.model.User;
import io.github.zkhan93.hisab.model.callback.GroupItemClickClbk;
import io.github.zkhan93.hisab.ui.adapter.GroupsAdapter;
import io.github.zkhan93.hisab.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupsActivityFragment extends Fragment implements GroupItemClickClbk {
    public static final String TAG = GroupsActivityFragment.class.getSimpleName();

    //member views
    @BindView(R.id.groups)
    RecyclerView groupList;
    //other members
    private GroupsAdapter groupsAdapter;
    private User me;

    public GroupsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = Util.getUser(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        ButterKnife.bind(this, rootView);

        groupList.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupsAdapter = new GroupsAdapter(this, me);
        groupList.setAdapter(groupsAdapter);
        return rootView;
    }

    @Override
    public void GroupClicked(String groupId, String groupName) {
        Intent intent = new Intent(getActivity(), DetailGroupActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        groupsAdapter.registerChildEventListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        groupsAdapter.unregisterChildEventListener();
        groupsAdapter.clear();
    }

}
