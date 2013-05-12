package com.dddesenvolvendo.invasaoalienigena.domain;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.dddesenvolvendo.invasaoalienigena.domain.comum.AnimatedCellEntity;
import com.dddesenvolvendo.invasaoalienigena.util.Direction;



public class Robo extends AnimatedCellEntity {

	final private int tamanhoPasso= 10;
	private Direction ultimaDirecao;
	private boolean roboParado = true;
	
	
	public Robo(int pCellX, int pCellY, int pWidth, int pHeight, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pCellX, pCellY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		
	}
	
	public void setDirecao(final Direction pDirection) {
		this.roboParado = false;
		if(ultimaDirecao == null || ultimaDirecao != pDirection)
		{
			ultimaDirecao = pDirection;
			switch(pDirection) {
				case UP:
					//this.setRotation(180);
					this.praCima();
					break;
				case DOWN:
					//this.setRotation(0);
					this.praBaixo();
					break;
				case LEFT:
					//this.setRotation(90);
					this.praEsquerda();
					break;
				case RIGHT:
					//this.setRotation(270);
					this.praDireita();
					break;
			}
		}
		
	}
	
	public void praEsquerda()
	{
		this.animate(new long[] { 250, 250, 250, 250, 250 }, 0, 4, !roboParado);
	}
	
	public void praBaixo()
	{
		this.animate(new long[] { 250, 250, 250, 250, 250 }, 5, 9, !roboParado);
	}
	

	public void praCima()
	{
		this.animate(new long[] { 250, 250, 250, 250, 250 }, 10, 14, !roboParado);
	}
	
	public void praDireita()
	{
		this.animate(new long[] { 250, 250, 250, 250, 250 }, 15, 19, !roboParado);
	}
	
	public void MoveRobo()
	{
		if(!roboParado)
		{
			switch(ultimaDirecao) {
			case UP:
				this.setPosition( this.getX() , this.getY() + tamanhoPasso );
				break;
			case DOWN:
				this.setPosition( this.getX() , this.getY() - tamanhoPasso );
				break;
			case LEFT:
				this.setPosition( this.getX() - tamanhoPasso  , this.getY());
				break;
			case RIGHT:
				this.setPosition( this.getX() + tamanhoPasso  , this.getY());
				break;
			}
		}
	}
	
	public void PararRobo()
	{
		this.roboParado = true;
		if(ultimaDirecao != null)
		{
			switch(ultimaDirecao) {
			case UP:
				//this.setRotation(180);
				this.praCima();
				break;
			case DOWN:
				//this.setRotation(0);
				this.praBaixo();
				break;
			case LEFT:
				//this.setRotation(90);
				this.praEsquerda();
				break;
			case RIGHT:
				//this.setRotation(270);
				this.praDireita();
				break;
			}
		}
		
	}



}
