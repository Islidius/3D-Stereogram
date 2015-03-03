package pack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {
	
	BufferedImage img; // final Image
	BufferedImage pattern; // pattern Image
	BufferedImage depth; // depth field (inlcudes text)
	
	int standart = 140; // pattern tile width
	int tile = 100;
	
	Random r = new Random();

	public Main(){
		img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB); // init images
		depth = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB); // both need to have the same resolution
		pattern = new BufferedImage(standart,standart,BufferedImage.TYPE_INT_RGB);
		
		gen_pattern(); // generate pattern
		
		gen_background(); // store patter to background
		
		gen_foreground(); // gernerate the depth field
		
		gen_stereograph(); // apply the effect
		
		try {
			File output = new File("output.jpg"); // saving the file
			ImageIO.write(img,"jpg",output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void gen_pattern(){ // a standart grayscale noise generator
		for(int i = 0;i<standart;i++){
			for(int j = 0;j<standart;j++){
				int f = r.nextInt(255);
				Color c3 = new Color(f,f,f);
				pattern.setRGB(i, j, c3.getRGB());
			}
		}
	}
	
	public void gen_background(){ // apply pattern to background but only first tile
		for(int i = 0;i<standart;i++){
			for(int j = 0;j<img.getHeight();j++){
				img.setRGB(i, j, pattern.getRGB(i%standart, j%standart));		
			}
		}
	}
	
	public void gen_foreground(){ // generate the depth field
		for(int i = 0;i<depth.getWidth();i++){
			for(int j = 0;j<depth.getHeight();j++){
				Color c1 = new Color(10,100,100);
				depth.setRGB(i, j, c1.getRGB()); // fill with background color
			}
		}
		
		Graphics2D g2d = depth.createGraphics();
		g2d.setColor(new Color(20,60,60));
		g2d.setFont(new Font("Serif",Font.BOLD,120)); // add the text
		g2d.drawString("GitHub", 20, 250);
		g2d.dispose();
	}
	
	public void gen_stereograph(){ // the effect
		for(int i = 0;i<img.getWidth();i++){
			for(int j = 0;j<img.getHeight();j++){
				int de = new Color(depth.getRGB(i, j)).getRed(); // getting the depth from the depth image(stored im red component)
				if(i+tile < img.getWidth())img.setRGB(i+tile, j, img.getRGB(i, j)); // repetition of the background
				if(i+tile-de < img.getWidth())img.setRGB(i+tile-de, j, img.getRGB(i, j)); // real stereogram effect
			}
		}
		
	}
	
	public static void main(String[] args){
		new Main();
	}

}
