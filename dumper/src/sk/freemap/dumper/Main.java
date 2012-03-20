package sk.freemap.dumper;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
        JFrame f = new JFrame("dumper");
		f.setSize(800,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        SavePanel savePanel = new SavePanel();
        f.add("East", savePanel);

        String mwfUrl;
        if (args.length < 1)
        	mwfUrl = "http://195.28.70.134/kapor2/maps/mapa.mwf";
        else
        	mwfUrl = args[0];
        
		KatApplet k = new KatApplet(mwfUrl);
		f.add(k);
		
		k.init();
		k.start();
		k.setViewChangedObserver(new ViewChangedObserver(savePanel));
	}
}
