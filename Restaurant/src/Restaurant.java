import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

class Order {
	String orderName;

	public Order(String orderName) {
		this.orderName = orderName;
	}

	public String getorderName() {
		return this.orderName;
	}

	public void setName(String orderName) {
		this.orderName = orderName;
	}
}

public class Restaurant extends JFrame {
	private JLabel nameLabel = new JLabel("Order:");
	private JTextField nameTextField = new JTextField(10);
	private JButton b = new JButton("REQUEST ORDER");
	private JButton c = new JButton("CANCEL");
	private JOptionPane sent = new JOptionPane("Meal order sent!");
	ArrayList<Order> orderList = new ArrayList<Order>();
	public Order order1;

	public Restaurant() 
	{
		this.setLayout(new MigLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(nameLabel);
		this.add(nameTextField, "wrap paragraph, grow");

		this.add(b);
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String orderName = nameTextField.getText();
				Order order = new Order(orderName);
				order1 = order;
				orderList.add(order1);
				nameTextField.setText("");
				System.out.println("Order ID:" + " " + orderList); //just to see the orders list being updated
				JOptionPane.showMessageDialog(Restaurant.this,
						"Order sent!",
						"Meal",
						JOptionPane.PLAIN_MESSAGE);
			}

		});

		this.add(c);
		c.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Restaurant.this.dispose();
			}

		});

		this.pack();
	}

	public static void main(String[] args) {
		Restaurant r = new Restaurant();
		Waiter w = new Waiter(r);
		Chef c = new Chef(r,w);

		String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(systemLookAndFeelClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		r.pack();
		r.setLocationRelativeTo(null);
		r.setVisible(true);
	}
}

class Waiter extends Thread {
	private Restaurant restaurant;
	public Waiter(Restaurant r) {
		restaurant = r;
		start();
	}
	public void run() {
		while(true) {
			for (int i = 0; i<1; i++) {
				while(restaurant.order1 == null) // no orders 
					synchronized(this) {
						try {
							wait(); // waits inputed for order
						} catch(InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				System.out.println( "Waiter recieved: " +
						restaurant.order1.orderName);
				restaurant.order1 = null;
			}} // end of loop
	}
}

class Chef extends Thread {
	private Restaurant restaurant;
	private Waiter waiter;
	String orderName;
	public Chef(Restaurant r, Waiter w) {
		restaurant = r;
		waiter = w;
		start();
	}
	public void run() {
		while(true) {
			for (int i = 0; i<1; i++) {
				if(restaurant.order1 == null) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				else {
					System.out.println("Chef is preparing:" + " " + restaurant.order1.orderName + "...");
					try {
						sleep(1000); //just to make it seem more realistic and less rushed
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized(waiter) {
						waiter.notify(); // notifies waiter
					}
				} }
			try {
				sleep(1000);
			} catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		} // end of loop
	}
}
