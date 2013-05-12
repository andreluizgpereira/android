package com.dddesenvolvendo.invasaoalienigena;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;

import com.dddesenvolvendo.invasaoalienigena.domain.Robo;
import com.dddesenvolvendo.invasaoalienigena.util.Constants;
import com.dddesenvolvendo.invasaoalienigena.util.Direction;

public class MainActivity extends SimpleBaseGameActivity implements Constants  {

	protected boolean mGameRunning;
	
	private static final int CAMERA_WIDTH = CELLS_HORIZONTAL * CELL_WIDTH; // 640
	private static final int CAMERA_HEIGHT = CELLS_VERTICAL * CELL_HEIGHT; // 480

	private static final int LAYER_COUNT = 4;

	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_BLOCO = LAYER_BACKGROUND + 1;
	private static final int LAYER_ROBO = LAYER_BLOCO + 1;
	private static final int LAYER_SCORE = LAYER_ROBO + 1;
	
	private Camera mCamera;

	private DigitalOnScreenControl mDigitalOnScreenControl;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	

	private Font mFont;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mRoboTextureRegion;
	
	
	private BitmapTextureAtlas mBackgroundTexture;
	private ITextureRegion mBackgroundTextureRegion;
	
	private Scene mScene;
	
	private Robo robo;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}
	
	@Override
	public void onGameCreated() {

	}
	
	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		/* Load all the textures this game needs. */
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200, 160);
		
		this.mRoboTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "robo200x160andando.png", 0, 0, 5,4);
		
		this.mBitmapTextureAtlas.load();
		
		this.mBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1008, 696);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "tiledMapBkg.png", 0, 0);
		this.mBackgroundTexture.load();
		
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base2.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
		
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		for(int i = 0; i < LAYER_COUNT; i++) {
			this.mScene.attachChild(new Entity());
		}

		/* Cria um background apartir de um sprite. */
		this.mScene.setBackgroundEnabled(false);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));
		
		/* Cria a imagem do robô. */
		this.robo = new Robo(0, 0, 60 , 60, this.mRoboTextureRegion, this.getVertexBufferObjectManager());
		//this.robo.animate(250);
		this.mScene.getChildByIndex(LAYER_ROBO).attachChild(this.robo);
		
		
		/* Controle direcional */
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if(pValueX == 1) {
					MainActivity.this.robo.setDirecao(Direction.RIGHT);
				} else if(pValueX == -1) {
					MainActivity.this.robo.setDirecao(Direction.LEFT);
				} else if(pValueY == 1) {
					MainActivity.this.robo.setDirecao(Direction.UP);
				} else if(pValueY == -1) {
					MainActivity.this.robo.setDirecao(Direction.DOWN);
				}
				else
					MainActivity.this.robo.PararRobo();
				
			}
		});
		/* Make the controls semi-transparent. */
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);

		this.mScene.setChildScene(this.mDigitalOnScreenControl);
		
		/* Make the Snake move every 0.5 seconds. */
		this.mScene.registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(MainActivity.this.mGameRunning) {
					//try {
						MainActivity.this.robo.MoveRobo();
						MainActivity.this.robo.PararRobo();
					//} catch (final SnakeSuicideException e) {
					//	SnakeGameActivity.this.onGameOver();
					//}

						MainActivity.this.handleNewRoboPosition();
				}
			}
		}));
		
		this.robo.PararRobo();
		MainActivity.this.mGameRunning = true;
		return this.mScene;
	}
	
	private void handleNewRoboPosition() {
		/*final SnakeHead snakeHead = this.mSnake.getHead();

		if(snakeHead.getCellX() < 0 || snakeHead.getCellX() >= CELLS_HORIZONTAL || snakeHead.getCellY() < 0 || snakeHead.getCellY() >= CELLS_VERTICAL) {
			this.onGameOver();
		} else if(snakeHead.isInSameCell(this.mFrog)) {
			this.mScore += 50;
			this.mScoreText.setText("Score: " + this.mScore);
			this.mSnake.grow();
			this.mMunchSound.play();
			this.setFrogToRandomCell();
		}*/
	}

}
