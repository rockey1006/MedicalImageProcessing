package medical;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.*;
import java.sql.*;

public class MedicalImageProcess extends JFrame implements ActionListener
{
	JPanel jp1,jp11,jp111,jp112,jp2,jp21,jp211,jp212,northPanel,pictPanel,pict1,pict2;
	JTabbedPane jtp;
	JButton xianxing,fanzhuan,weicaise,pinghua,ruihua,zhongzhi,erzhihua,bianyuan;
	TitledBorder titled1,titled2,titled3,titled4;
	JSlider jSlider;
	JMenuBar bar;
	JMenu fileMenu,dataMenu;
	JMenuItem openItem,exitItem,checkItem,insertItem;
	JFileChooser chooser;
	File file;
	String filename;
	Image im,tmp,tmp0;
	int i,iw,ih;
	int[] pixels;
	Border etched1,etched2;
	SoftBevelBorder softBevelBorder;
	Connection con;
	Statement stmt; 
	ResultSet rs;
	String id,name,pi;
	boolean flagGray = false;
	boolean flagLoad = false;
	boolean flag=false;
	
	public MedicalImageProcess()
	{
		super("Medical Image Processing");
		
		Container contents = getContentPane();//create a container 
		
		jtp = new JTabbedPane(SwingConstants.TOP);//create a tab to switch groups of components
		
		jp1 = new JPanel();
		jp11 = new JPanel();
		jp111 = new JPanel();
		jp112 = new JPanel();
		
		jp2 = new JPanel();
		jp21 = new JPanel();
		jp211 = new JPanel();
		jp212 = new JPanel();
		
		northPanel = new JPanel();
		pictPanel = new JPanel();
		pict1 = new JPanel();
		pict2 = new JPanel();

		jSlider = new JSlider(JSlider.HORIZONTAL,0,255,100);//Create a moving slider to choose value
			
		xianxing = new JButton("Linear Grey Level Transformation");
		fanzhuan = new JButton("Image Negatives");
		weicaise = new JButton("Pseudocolor Image Processing");
		pinghua = new JButton("Smoothing");
		ruihua = new JButton("Sharpening");
		zhongzhi = new JButton("Median Filter");
		erzhihua = new JButton("Detection of Isolated Points");
		bianyuan = new JButton("Edge Detection");
		
		titled1 = new TitledBorder("Grey Level Transformation and Pseudocolor ");
		titled2 = new TitledBorder("Smoothing and Sharpening and Medican filter");
		titled3 = new TitledBorder("Image Threshold");
		titled4 = new TitledBorder("Image Robert Gradient Operators");
			
    	softBevelBorder = new SoftBevelBorder(2,Color.black,Color.white);
    	
		init();
			
		jp111.setBorder(titled1);//Set the titled border
		jp112.setBorder(titled2);
		jp211.setBorder(titled3);
		jp212.setBorder(titled4);
		
		jSlider.setMajorTickSpacing(50);//setMajorTickSpacing
		jSlider.setMinorTickSpacing(10);//setMinorTickSpacing
		jSlider.setPaintTicks(true);//setPaintTicks
		jSlider.setPaintTrack(true);//setPaintTrack
		jSlider.setPaintLabels(true);//setPaintLabels
		
		jp111.add(xianxing);
		jp111.add(fanzhuan);
		jp111.add(weicaise);
		jp112.add(pinghua);
	    jp112.add(ruihua);
		jp112.add(zhongzhi);
		jp11.setLayout(new GridLayout(1,2));
		jp11.add(jp111);
		jp11.add(jp112);
		
		jp211.add(jSlider);
		jp211.add(erzhihua);
		jp212.add(bianyuan);
		jp21.setLayout(new GridLayout(1,2));
		jp21.add(jp211);
		jp21.add(jp212);
		
		
		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new BorderLayout());
			
		pictPanel.setBorder(softBevelBorder);
		
		northPanel.setLayout(new BorderLayout());
		
		jp1.add(jp11);
		jp2.add(jp21);
			
		jtp.add("Image Intensity",jp1);
		jtp.add("Image Segmentation",jp2);
			
		northPanel.add(jtp,BorderLayout.CENTER);
	    etched1 = BorderFactory.createEtchedBorder();//create a EtchedBorder
	    etched2 = BorderFactory.createEtchedBorder();
        pictPanel.setLayout(new GridLayout(0,2));
		pict1.setBorder(etched1);
		pict2.setBorder(etched2);

		pictPanel.add(pict1);
		pictPanel.add(pict2);
	   	contents.add(northPanel,BorderLayout.NORTH);
		contents.add(pictPanel,BorderLayout.CENTER);
		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con = DriverManager.getConnection("jdbc:odbc:image");
			stmt = con.createStatement();
		}
		catch(Exception e){}
		
		xianxing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jxianxing_ActionPerformed(e);
			}
		});
		
		fanzhuan.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jfanzhuan_ActionPerformed(e);
			}
		});
		
		weicaise.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jweicaise_ActionPerformed(e);
			}
		});
		
		pinghua.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jpinghua_ActionPerformed(e);
			}
		});
		ruihua.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jruihua_ActionPerformed(e);
			}
		});
		
		zhongzhi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jzhongzhi_ActionPerformed(e);
			}
		});
		
		jSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e)
			{
				jerzhihua_ActionPerformed(e);
			}
		});
		
		erzhihua.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jerzhihua_ActionPerformed(e);
			}
		});
		
		bianyuan.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jbianyuan_ActionPerformed(e);
			}
		});
	}

	public void init()
	{
		bar = new JMenuBar();//create a menu bar
		setJMenuBar( bar );
		
		fileMenu = new JMenu("File");//create a fileMenu
		fileMenu.setMnemonic('F');
		openItem = new JMenuItem("Open");//MenuItem
		openItem.setMnemonic('O');
		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('X');
		
		dataMenu = new JMenu("Data");
		dataMenu.setMnemonic('D');
		checkItem = new JMenuItem("Check");
		checkItem.setMnemonic('C');
		insertItem = new JMenuItem("Insert");
		insertItem.setMnemonic('I');
		
		fileMenu.add(openItem);
		fileMenu.add(exitItem);
		
		dataMenu.add(checkItem);
		dataMenu.add(insertItem);
		
		bar.add(fileMenu);
		bar.add(dataMenu);
		
		openItem.addActionListener(this);
		exitItem.addActionListener(this);
		checkItem.addActionListener(this);
		insertItem.addActionListener(this);

	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == openItem)
		{
			chooser = new JFileChooser("F:");//file chooser
			chooser.addChoosableFileFilter(new MyFileFilter("jpg;gif","image format"));
		    	    
		    int returnVal = chooser.showOpenDialog(pictPanel);
		    if(returnVal == JFileChooser.APPROVE_OPTION)//The return value 
		    {
		    	file = chooser.getSelectedFile();//Returns the selected file
		    	filename = file.getPath();//Converts this abstract pathname into a pathname string
		       	
		       	jLoad_ActionPerformed(event);
		    }			
		}
		else if(event.getSource() == exitItem)
	    {
			int i = JOptionPane.showConfirmDialog(null,"To exit the system?","exit",2,2);
		
			if(i == 0)//If the user presses the OK
			{
				try
				{
					rs.close();    //close the record set
					stmt.close(); //close the statement target
					con.close();  //close the conserve
				}	
				catch(Exception e6){}
				this.dispose();//dispose resources
				System.exit(0);
			}	    	
	    }
	    else if(event.getSource() == checkItem)
		{
			try
			{
				int rec=0;
				String s=JOptionPane.showInputDialog(null,"Please enter the ID");
				rs = stmt.executeQuery("SELECT ID, name,image FROM pic");
				while (rs.next())
				{
					id= rs.getString("ID");
					name= rs.getString("name");
					pi=rs.getString("image");
					if(id.equals(s.trim()))
					{
		        	    filename=pi;
		            	jLoad_ActionPerformed(event);
						rec=1;
						break;
					}
				}
				if(rec==0)JOptionPane.showMessageDialog(null,"This image is not in the database");
				rs.close();					
			}
		    catch(Exception e2){}
		}
		else if(event.getSource() == insertItem)
		{
			try
			{
				String s1=JOptionPane.showInputDialog(null,"Please enter the ID");
				String s2 = JOptionPane.showInputDialog(null,"Please enter the name");
				String strInc = "INSERT INTO pic(ID,name,image) Values('"+s1+"','"+s2+"','"+filename+"')";
				stmt.executeUpdate(strInc);
				
			}
			catch(Exception e3){}	
			finally{
				JOptionPane.showMessageDialog(null,"The image has been added to the database");
			}		
		}
			
	}
	
	public void jLoad_ActionPerformed(ActionEvent e)
	{
		//Use MediaTracker track image loading
		MediaTracker tracker = new MediaTracker(this);
		im=Toolkit.getDefaultToolkit().getImage(filename);
		tracker.addImage(im,0);//im is tracking image£¬0 is the image tracking identifier 
	
		//Wait for the image has fully loaded
		try{
		tracker.waitForID(0);//This 0 of 0 corresponds to the above
		}catch(InterruptedException e2){ e2.printStackTrace();}//The terminal output to the standard error stream
	
		//Get image width and height ih and iw
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		pixels=new int[iw*ih];
		
		try{// obtain an image from the im stored in the array of pixels
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();//request to start grab pixels
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		
		//The array of pixels to produce an image, use the default RGB
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);//creates an image by the specified image producer ip 
		flagLoad = true;
		repaint();
	}
	
	public void jxianxing_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
			//The ColorModel abstract class encapsulates the converted pixel value to color components (for example, red, green and blue) and an alpha component method.
			ColorModel cm=ColorModel.getRGBdefault();//Returns a default RGB value of the object
			for(i=0;i<ih*iw;i++)
			{
				int alpha=cm.getAlpha(pixels[i]);
				int red=cm.getRed(pixels[i]);
				int green=cm.getGreen(pixels[i]);
				int blue=cm.getBlue(pixels[i]);
		
				red=(int)(1.1*red+20);
				green=(int)(1.1*green+20);
				blue=(int)(1.1*blue+20);
				if(red>=255){red=255;}	
				if(green>=255){green=255;}
				if(blue>=255){blue=255;}
		
				pixels[i]=alpha<<24|red<<16|green<<8|blue;
			}
			//let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flagGray=true;
			flag = true;
			repaint();
				
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
	                         "note",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void jfanzhuan_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
			//The ColorModel abstract class encapsulates the converted pixel value to color components (for example, red, green and blue) and an alpha component method.
			ColorModel cm=ColorModel.getRGBdefault();//Returns a default RGB value of the object
			for(i=0;i<ih;i++)
			{
				for(int j=0;j<iw;j++)
				{
					int alpha=cm.getAlpha(pixels[i*iw+j]);
					int red=255-cm.getRed(pixels[i*iw+j]);
					int green=255-cm.getGreen(pixels[i*iw+j]);
					int blue=255-cm.getBlue(pixels[i*iw+j]);
					
					pixels[i*iw+j]=alpha<<24|red<<16|green<<8|blue;
				}
			}
			//Let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flagGray=true;
			flag = true;
			repaint();
				
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
	                         "note",JOptionPane.WARNING_MESSAGE);
		}
	}
	
    public void jweicaise_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) 
			{
				e3.printStackTrace();
			}
				
			ColorModel cm=ColorModel.getRGBdefault();
			
			for(int i=0;i<ih;i++)
			{
				for(int j=0;j<iw;j++)
				{
					int grey = pixels[i*iw+j]&0xff;
					int alpha=cm.getAlpha(pixels[i*iw+j]);
					int red,green ,blue ;
					//method£¨1£©
   	 /*             if(grey<64)
				    {
				    	 red = 0;
				    	 green = 4 * grey;
				    	 blue = 255;
				    }
				    else if(grey<128)
				    {
				    	 red = 0;
				    	 green = 255;
				         blue = (127-grey)*4;			   
				    }
				    else if(grey<192)
				    {
				    	 red = (grey-128)*4;
			    		 green = 255;
			        	 blue = 0;
			    	}
			    	else 
			    	{
			    		 red = 255;
			    	 	 green = (255-grey)*4;
			    	 	 blue = 0;
			        }
	*/
				    //method£¨2£©
	/*		   	    switch(grey/64)
				    {
			  		  	case 0:red = 0;green = 4 * green;blue = 255;break;
			  		  	case 1:red = 0;green = 255;blue = 511 - 4 * grey;break;
			 		   	case 2:red = 4 * grey - 511;green = 255;blue = 0;break;
			  		  	case 3:red = 255;green = 1023 - 4 * grey;blue = 0;break;
			 	    }  
	*/		    
	//		 	    pixels[i*iw+j]=alpha<<24|red<<16|green<<8|blue;
	
			 	   //method£¨3£©
	/*		 	   int[] colorTable = {0x000000,0x000055,0x005500,0x550000,0x3F3F3F,0x550055,0x0000FF,
			 	   0x555500,0x00FF00,0xFF0000,0x808080,0x00FFFF,0xFFFF00,0xFFFFFF,0x005555,0xFF00FF};
			 	   int newGrey = grey * 16 / 255;
			 	   pixels[i*iw+j]=alpha<<24|colorTable[newGrey];
	*/		     
			  	   //method(4)
				   int[] colorTable = {0x000000,0x0000a8,0x00a800,0x00a8a8,0xa80000,0xa800a8,0xa85400,0xa8a8a8,
	                                   0x545454,0x5454fc,0x54fc54,0x54fcfc,0xfc5454,0xfc54fc,0xfcfc54,0xfcfcfc,
    							       0x000000,0x141414,0x202020,0x2c2c2c,0x383838,0x444444,0x505050,0x606060,
									   0x707070,0x808080,0x909090,0xa0a0a0,0xb4b4b4,0xc8c8c8,0xe0e0e0,0xfcfcfc,
									   0x0000fc,0x4000fc,0x7c00fc,0xbc00fc,0xfc00fc,0xfc00bc,0xfc007c,0xfc0040,
									   0xfc0000,0xfc4000,0xfc7c00,0xfcbc00,0xfcfc00,0xbcfc00,0x7cfc00,0x40fc00,
									   0x00fc00,0x00fc40,0x00fc7c,0x00fcbc,0x00fcfc,0x00bcfc,0x007cfc,0x0040fc,
									   0x7c7cfc,0x9c7cfc,0xbc7cfc,0xdc7cfc,0xfc7cfc,0xfc7cdc,0xfc7cbc,0xfc7c9c,
									   0xfc7c7c,0xfc9c7c,0xfcbc7c,0xfcdc7c,0xfcfc7c,0xdcfc7c,0xbcfc7c,0x9cfc7c,
									   0x7cfc7c,0x7cfc9c,0x7cfcbc,0x7cfcdc,0x7cfcfc,0x7cdcfc,0x7cbcfc,0x7c9cfc,
									   0xb4b4fc,0xc4b4fc,0xd8b4fc,0xe8b4fc,0xfcb4fc,0xfcb4e8,0xfcb4d8,0xfcb4c4,
									   0xfcb4b4,0xfcc4b4,0xfcd8b4,0xfce8b4,0xfcfcb4,0xe8fcb4,0xd8fcb4,0xc4fcb4,
									   0xb4fcb4,0xb4fcc4,0xb4fcd8,0xb4fce8,0xb4fcfc,0xb4e8fc,0xb4d8fc,0xb4c4fc,
									   0x000070,0x1c0070,0x380070,0x540070,0x700070,0x700054,0x700038,0x70001c,
									   0x700000,0x701c00,0x703800,0x705400,0x707000,0x547000,0x387000,0x1c7000,
									   0x007000,0x00701c,0x007038,0x007054,0x007070,0x005470,0x003870,0x001c70,
									   0x383870,0x443870,0x543870,0x603870,0x703870,0x703860,0x703854,0x703844,
									   0x703838,0x704438,0x705438,0x706038,0x707038,0x607038,0x547038,0x447038,
									   0x387038,0x387044,0x387054,0x387060,0x387070,0x386070,0x385470,0x384470,
									   0x505070,0x585070,0x605070,0x685070,0x705070,0x705068,0x705060,0x705058,
									   0x705050,0x705850,0x706050,0x706850,0x707050,0x687050,0x607050,0x587050,
								       0x507050,0x507058,0x507060,0x507068,0x507070,0x506870,0x506070,0x505870,
									   0x000040,0x100040,0x200040,0x300040,0x400040,0x400030,0x400020,0x400010,
									   0x400000,0x401000,0x402000,0x403000,0x404000,0x304000,0x204000,0x104000,
									   0x004000,0x004010,0x004020,0x004030,0x004040,0x003040,0x002040,0x001040,
									   0x202040,0x282040,0x302040,0x382040,0x402040,0x402038,0x402030,0x402028,
									   0x402020,0x402820,0x403020,0x403820,0x404020,0x384020,0x304020,0x284020,
									   0x204020,0x204028,0x204030,0x204038,0x204040,0x203840,0x203040,0x202840,
									   0x2c2c40,0x302c40,0x342c40,0x3c2c40,0x402c40,0x402c3c,0x402c34,0x402c30,
									   0x402c2c,0x40302c,0x40342c,0x403c2c,0x40402c,0x3c402c,0x34402c,0x30402c,
									   0x2c402c,0x2c4030,0x2c4034,0x2c403c,0x2c4040,0x2c3c40,0x2c3440,0x2c3040,
									   0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000};
					if(grey<16)
  					{
  				        pixels[i*iw+j]=alpha<<24|colorTable[grey];
  					}
  					if(grey>=16&&grey<255)
  					{
  				        pixels[i*iw+j]=alpha<<24|colorTable[30+grey/4];
  					}
  					    
				}
			}
    		//let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag=true;
			repaint();
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
                         "note",JOptionPane.WARNING_MESSAGE);
	 	 	}
	}

	public void jpinghua_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
					
			//smoothing process, Alpha value remains unchanged
			ColorModel cm=ColorModel.getRGBdefault();
			for(int i=1;i<ih-1;i++)
			{
				for(int j=1;j<iw-1;j++)
				{       
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				int red=cm.getRed(pixels[i*iw+j]);
				int green=cm.getGreen(pixels[i*iw+j]);
				int blue=cm.getBlue(pixels[i*iw+j]);
				
				//Image smoothing
				int red1=cm.getRed(pixels[(i-1)*iw+j-1]);
				int red2=cm.getRed(pixels[(i-1)*iw+j]);
				int red3=cm.getRed(pixels[(i-1)*iw+j+1]);
				int red4=cm.getRed(pixels[i*iw+j-1]);
				int red6=cm.getRed(pixels[i*iw+j+1]);
				int red7=cm.getRed(pixels[(i+1)*iw+j-1]);
				int red8=cm.getRed(pixels[(i+1)*iw+j]);
				int red9=cm.getRed(pixels[(i+1)*iw+j+1]);
				int averageRed=(red1+red2+red3+red4+red6+red7+red8+red9)/8;
				
				int green1=cm.getGreen(pixels[(i-1)*iw+j-1]);
				int green2=cm.getGreen(pixels[(i-1)*iw+j]);
				int green3=cm.getGreen(pixels[(i-1)*iw+j+1]);
				int green4=cm.getGreen(pixels[i*iw+j-1]);
				int green6=cm.getGreen(pixels[i*iw+j+1]);
				int green7=cm.getGreen(pixels[(i+1)*iw+j-1]);
				int green8=cm.getGreen(pixels[(i+1)*iw+j]);
				int green9=cm.getGreen(pixels[(i+1)*iw+j+1]);
				int averageGreen=(green1+green2+green3+green4+green6+green7+green8+green9)/8;
				
				int blue1=cm.getBlue(pixels[(i-1)*iw+j-1]);
				int blue2=cm.getBlue(pixels[(i-1)*iw+j]);
				int blue3=cm.getBlue(pixels[(i-1)*iw+j+1]);
				int blue4=cm.getBlue(pixels[i*iw+j-1]);
				int blue6=cm.getBlue(pixels[i*iw+j+1]);
				int blue7=cm.getBlue(pixels[(i+1)*iw+j-1]);
				int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
				int blue9=cm.getBlue(pixels[(i+1)*iw+j+1]);
				int averageBlue=(blue1+blue2+blue3+blue4+blue6+blue7+blue8+blue9)/8;
				
				pixels[i*iw+j]=alpha<<24|averageRed<<16|averageGreen<<8|averageBlue;
				}
			}
			
			//Let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag=true;
			repaint();
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
	                         "note",JOptionPane.WARNING_MESSAGE);
		  	}
	}
			
	public void jruihua_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
			
			//The image sharpening process, Alpha value remains unchanged
			ColorModel cm=ColorModel.getRGBdefault();
			for(int i=1;i<ih-1;i++)
			{
				for(int j=1;j<iw-1;j++)
				{
				int alpha=cm.getAlpha(pixels[i*iw+j]);

				//sharpening process
				int red6=cm.getRed(pixels[i*iw+j+1]);
				int red5=cm.getRed(pixels[i*iw+j]);
				int red8=cm.getRed(pixels[(i+1)*iw+j]);
				int sharpRed=Math.abs(red6-red5)+Math.abs(red8-red5);
				
				int green5=cm.getGreen(pixels[i*iw+j]);
				int green6=cm.getGreen(pixels[i*iw+j+1]);
				int green8=cm.getGreen(pixels[(i+1)*iw+j]);
				int sharpGreen=Math.abs(green6-green5)+Math.abs(green8-green5);
				
				int blue5=cm.getBlue(pixels[i*iw+j]);
				int blue6=cm.getBlue(pixels[i*iw+j+1]);
				int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
				int sharpBlue=Math.abs(blue6-blue5)+Math.abs(blue8-blue5);
				
				if(sharpRed>255) {sharpRed=255;}
				if(sharpGreen>255) {sharpGreen=255;}
				if(sharpBlue>255) {sharpBlue=255;}
				
				pixels[i*iw+j]=alpha<<24|sharpRed<<16|sharpGreen<<8|sharpBlue;
				}
			}
			
			//Let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag=true;
			repaint();
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
		                 "note",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void jzhongzhi_ActionPerformed(ActionEvent e)
	{
		if(flagLoad)
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
					
			//Median filtering processing, Alpha value remains unchanged
			ColorModel cm=ColorModel.getRGBdefault();
			for(int i=1;i<ih-1;i++)
			{
				for(int j=1;j<iw-1;j++)
				{
				int red,green,blue;
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				
				int red2=cm.getRed(pixels[(i-1)*iw+j]);
				int red4=cm.getRed(pixels[i*iw+j-1]);
				int red5=cm.getRed(pixels[i*iw+j]);
				int red6=cm.getRed(pixels[i*iw+j+1]);
				int red8=cm.getRed(pixels[(i+1)*iw+j]);
				
				//Horizontal median filtering
				if(red4>=red5){
					if(red5>=red6) {red=red5;}
					else{
					if(red4>=red6) {red=red6;}
					else{red=red4;}
				}}
				else{
				if(red4>red6) {red=red4;}
					else{
					if(red5>red6) {red=red6;}
					else{red=red5;}
				}}
					
				int green2=cm.getGreen(pixels[(i-1)*iw+j]);
				int green4=cm.getGreen(pixels[i*iw+j-1]);
				int green5=cm.getGreen(pixels[i*iw+j]);
				int green6=cm.getGreen(pixels[i*iw+j+1]);
				int green8=cm.getGreen(pixels[(i+1)*iw+j]);
				
				//Horizontal median filtering
				if(green4>=green5){
					if(green5>=green6) {green=green5;}
					else{
					if(green4>=green6) {green=green6;}
					else{green=green4;}
				}}
				else{
				if(green4>green6) {green=green4;}
					else{
					if(green5>green6) {green=green6;}
					else{green=green5;}
				}}
					
				
				int blue2=cm.getBlue(pixels[(i-1)*iw+j]);
				int blue4=cm.getBlue(pixels[i*iw+j-1]);
				int blue5=cm.getBlue(pixels[i*iw+j]);
				int blue6=cm.getBlue(pixels[i*iw+j+1]);
				int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
				
				//Horizontal median filtering
				if(blue4>=blue5){
					if(blue5>=blue6) {blue=blue5;}
					else{
					if(blue4>=blue6) {blue=blue6;}
					else{blue=blue4;}
				}}
				else{
				if(blue4>blue6) {blue=blue4;}
					else{
					if(blue5>blue6) {blue=blue6;}
					else{blue=blue5;}
				}}
				pixels[i*iw+j]=alpha<<24|red<<16|green<<8|blue;
				}
			}
			
			//Let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag=true;
			repaint();
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
	                         "note",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void jerzhihua_ActionPerformed(ChangeEvent e)
	{
		if(flagLoad)
	    {
	    	try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
				
			//Set threshold value, the default value is 100
			int grey=jSlider.getValue();
					    
			//image segmentation of detection of isolated points
			ColorModel cm=ColorModel.getRGBdefault();
			for(int i=0;i<iw*ih;i++)
			{
				int red,green,blue;
				int alpha=cm.getAlpha(pixels[i]);
				if(cm.getRed(pixels[i])>grey)
				{
					red = 255;
				}else{ red=0;}
					
				if(cm.getGreen(pixels[i])>grey)
				{
					green=255;
				}else{green=0;}
					
				if(cm.getBlue(pixels[i])>grey)
				{
					blue=255;
				}else{blue=0;}
				
				pixels[i]=alpha<<24|red<<16|green<<8|blue;
			}
				
			//let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag = true;
			repaint();
		}else{
			 JOptionPane.showMessageDialog(null,"Please open a picture!",
		                 	"note",JOptionPane.WARNING_MESSAGE);
		}
	}
		
	public void jerzhihua_ActionPerformed(ActionEvent e)
	{
		if(flagLoad) 
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
				
			//Set threshold value, the default value is 100
			int grey=100;
			Object tmpGrey="100";
			String s=JOptionPane.showInputDialog(null,"Enter the threshold£¨0-255£©£º",tmpGrey);
		  
			if(s!=null)
			{
		    	grey=Integer.parseInt(s);
		    }
			if(grey>255)
			{
		    	grey=255;
			}else if(grey<0)
			{
				grey=0;
			}
			//image threshold value processing
			ColorModel cm=ColorModel.getRGBdefault();
			for(int i=0;i<iw*ih;i++)
			{
				int red,green,blue;
				int alpha=cm.getAlpha(pixels[i]);
				if(cm.getRed(pixels[i])>grey)
				{
					red = 255;
				}else{ red=0;}
					
				if(cm.getGreen(pixels[i])>grey)
				{
					green=255;
				}else{green=0;}
					
				if(cm.getBlue(pixels[i])>grey)
				{
					blue=255;
				}else{blue=0;}
					
				pixels[i]=alpha<<24|red<<16|green<<8|blue;
			}
				
			//Let the array of pixels to produce an image
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);			
			tmp0=createImage(ip);
			flag = true;
			repaint();
		}else{
		 	JOptionPane.showMessageDialog(null,"Please open a picture!",
		                 	"note",JOptionPane.WARNING_MESSAGE);
		}
	}
		
	public void jbianyuan_ActionPerformed(ActionEvent e)
	{
		if(flagLoad) 
		{
			try{
			PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
			}catch (InterruptedException e3) {
				e3.printStackTrace();
			}
						
			//edge detection processing
			ColorModel cm=ColorModel.getRGBdefault();
			for(i=1;i<ih-1;i++)
			{
				for(int j=1;j<iw-1;j++)
				{
					//edge detection processing from the image
					int alpha=cm.getAlpha(pixels[i*iw+j]);
					int red5=cm.getRed(pixels[i*iw+j]);
					int red6=cm.getRed(pixels[i*iw+j+1]);
					int red8=cm.getRed(pixels[(i+1)*iw+j]);
					int red9=cm.getRed(pixels[(i+1)*iw+j+1]);
					
					int robertRed=Math.max(Math.abs(red5-red9),Math.abs(red8-red6));
					
					int green5=cm.getGreen(pixels[i*iw+j]);
					int green6=cm.getGreen(pixels[i*iw+j+1]);
					int green8=cm.getGreen(pixels[(i+1)*iw+j]);
					int green9=cm.getGreen(pixels[(i+1)*iw+j+1]);
					
					int robertGreen=Math.max(Math.abs(green5-green9),Math.abs(green8-green6));
					
					int blue5=cm.getBlue(pixels[i*iw+j]);
					int blue6=cm.getBlue(pixels[i*iw+j+1]);
					int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
					int blue9=cm.getBlue(pixels[(i+1)*iw+j+1]);
					
					int robertBlue=Math.max(Math.abs(blue5-blue9),Math.abs(blue8-blue6));
					
					pixels[i*iw+j]=alpha<<24|robertRed<<16|robertGreen<<8|robertBlue;
				}
			}
				
			
			ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
			tmp0=createImage(ip);
			flag = true;
			repaint();
		}else{
			JOptionPane.showMessageDialog(null,"Please open a picture!",
		  		"note",JOptionPane.WARNING_MESSAGE);
		}
	}
		
	public void paint(Graphics g)
	{
		if(flagLoad)
		{
			g.drawImage(tmp,9,171,this);
		}
		if(flagLoad&&flag)
		{
			g.drawImage(tmp0,9+pict1.getWidth(),171,this);
		}
	}
	
	public static void main(String[] args)
	{
		MedicalImageProcess mig = new MedicalImageProcess();
		mig.setLocation(30,30);
		mig.setSize(964,708);

		mig.setVisible(true);
	}
}