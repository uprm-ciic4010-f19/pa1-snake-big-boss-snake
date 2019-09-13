package Game.GameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Main.Handler;
import Resources.Images;
import UI.ClickListlener;
import UI.UIImageButton;
import UI.UIManager;

public class GameOverState extends State{

	private UIManager uiManager;
	
	public GameOverState(Handler handler) {
		super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);


        uiManager.addObjects(new UIImageButton(200, 450, 200, 100, Images.butrestart, new ClickListlener() {
            @Override
            public void onClick() {
                handler.getMouseManager().setUimanager(null);
                handler.getGame().reStart();
                State.setState(handler.getGame().gameState);
            }
        }));
	}

	@Override
	public void tick() {
		
		handler.getMouseManager().setUimanager(uiManager);
		uiManager.tick();
		
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			 State.setState(handler.getGame().menuState);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font ("ComicSans", Font.BOLD, 28));
		g.drawString("Press 'Escape' to see the Menu", 95, 450);
		g.drawImage(Images.GameOver,0,0,600,600,null);
		uiManager.Render(g);
		
	}

}
