package com.jookovjook.chatapp.new_new_pub;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.new_pub.Logo;
import com.jookovjook.chatapp.new_pub.LogoFlyoutMenu;

import org.zakariya.flyoutmenu.FlyoutMenuView;

import java.util.ArrayList;
import java.util.List;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.ViewHolder>{

    private ArrayList<LinksProvider> linksList;
    private Context context;
    private LinksAdapterCallback callback;

    public interface LinksAdapterCallback{
        void onTextAdded();
    }

    public LinksAdapter(Context context, ArrayList<LinksProvider> linksList, LinksAdapterCallback callback){
        this.context = context;
        this.linksList = linksList;
        this.callback = callback;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public EditText link;
        public FlyoutMenuView logoFlyoutMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            link = (EditText) itemView.findViewById(R.id.link);
            link.setSelected(false);
            logoFlyoutMenu = (FlyoutMenuView) itemView.findViewById(R.id.logoFlyoutMenu);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_item, parent, false);
        return new LinksAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LinksProvider linksProvider = linksList.get(position);
        configureSmileyFlyoutMenu(holder.logoFlyoutMenu, linksProvider);
        holder.link.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(linksProvider.link.length() == 0){
                    callback.onTextAdded();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linksProvider.link = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(linksProvider.link.length() == 0 & position != getItemCount() & getItemCount() > 0){
                    linksList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return linksList.size();
    }

    private void configureSmileyFlyoutMenu(FlyoutMenuView logoFlyoutMenu, final LinksProvider linkProvider) {

        final Logo[] logos = {Logo.web, Logo.bitbucket, Logo.facebook, Logo.github, Logo.youtube};

        List<LogoFlyoutMenu.MenuItem> menuItems = new ArrayList<>();

        for (int i = 0; i < logos.length; i++) {
            Logo logo = logos[i];
            menuItems.add(new LogoFlyoutMenu.MenuItem(menuItems.size(), logo, context));
        }

        logoFlyoutMenu.setLayout(new FlyoutMenuView.GridLayout(2, FlyoutMenuView.GridLayout.UNSPECIFIED));
        logoFlyoutMenu.setAdapter(new FlyoutMenuView.ArrayAdapter<>(menuItems));

        final LogoFlyoutMenu.ButtonRenderer renderer = new LogoFlyoutMenu.ButtonRenderer(linkProvider.logo, context);
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
