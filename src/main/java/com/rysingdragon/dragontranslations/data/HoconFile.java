package com.rysingdragon.dragontranslations.data;

import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class HoconFile {

    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode node;

    public HoconFile(HoconConfigurationLoader loader) {
        this.loader = loader;
    }

    public void save() {
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            this.node = this.loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void useValuesFromFile(HoconFile other) {
        this.node.mergeValuesFrom(other.getNode());
    }

    public CommentedConfigurationNode getNode(Object... path) {
        return node.getNode(path);
    }
}
