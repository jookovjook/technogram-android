package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jookovjook.chatapp.R;

import org.zakariya.flyoutmenu.FlyoutMenuView;

import java.util.ArrayList;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder>{

    private ArrayList<LinkProvider> nList;
    private Context mContext;


    class ViewHolder extends RecyclerView.ViewHolder{

        public EditText link;
        public FlyoutMenuView logoFlyoutMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            link = (EditText) itemView.findViewById(R.id.link);
            logoFlyoutMenu = (FlyoutMenuView) itemView.findViewById(R.id.logoFlyoutMenu);
        }
    }

    public LinkAdapter(ArrayList<LinkProvider> mList, Context mContext){
        this.nList = mList;
        this.mContext = mContext;
    }

    public void addLink(){
        nList.add(new LinkProvider());
    }

    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.link_item, parent, false);
        return new LinkAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LinkAdapter.ViewHolder holder, int position) {
        final LinkProvider linkProvider = nList.get(position);
        configureSmileyFlyoutMenu(holder.logoFlyoutMenu, linkProvider);
        holder.link.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linkProvider.link = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    void configureSmileyFlyoutMenu(FlyoutMenuView logoFlyoutMenu, final LinkProvider linkProvider) {

        final Logo[] logos = {Logo.web, Logo.bitbucket, Logo.facebook, Logo.github, Logo.youtube};

        List<LogoFlyoutMenu.MenuItem> menuItems = new ArrayList<>();

        for (int i = 0; i < logos.length; i++) {
            Logo logo = logos[i];
            menuItems.add(new LogoFlyoutMenu.MenuItem(menuItems.size(), logo, mContext));
        }

        logoFlyoutMenu.setLayout(new FlyoutMenuView.GridLayout(2, FlyoutMenuView.GridLayout.UNSPECIFIED));
        logoFlyoutMenu.setAdapter(new FlyoutMenuView.ArrayAdapter<>(menuItems));

        final LogoFlyoutMenu.ButtonRenderer renderer = new LogoFlyoutMenu.ButtonRenderer(linkProvider.logo, mContext);
        logoFlyoutMenu.setButtonRenderer(renderer);

        logoFlyoutMenu.setSelectionListener(new FlyoutMenuView.SelectionListener() {
            @Override
            public void onItemSelected(FlyoutMenuView flyoutMenuView, FlyoutMenuView.MenuItem item) {
                linkProvider.selectionId = item.getId();
                linkProvider.setLogo(logos[item.getId()]);
                renderer.setLogo(((LogoFlyoutMenu.MenuItem) item).getLogo());
            }

            @Override
            public void onDismissWithoutSelection(FlyoutMenuView flyoutMenuView) {
            }
        });

    }
}
