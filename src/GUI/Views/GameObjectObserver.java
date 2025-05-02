package GUI.Views;

import GUI.GameObjectEvent;

public interface GameObjectObserver {
        void update(GameObjectEvent event);
}
