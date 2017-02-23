import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aki on 2016-05-18.
 */
public class SnakeGame extends JComponent {

    public static final int gridwidth = 40;
    public static final int gridheight = 25;
    public static final int boxwidth = 20;
    public static final int boxheight = 20;
    public static final int windowwidth = 800;
    public static final int windowheight = 600;

    public int direction = Direction.nodirection;
    public int score = 0;
    public int level = 1;
    public int speed = 600;
    public int fps = 30;
    public boolean started = false;
    public boolean paused = false;

    public boolean loselength = false;
    public boolean opposite = false;
    public boolean opposite_ing = false;



    public LinkedList<Point> snake;
    public Point fruit;
    public Timer timer_repaint = new Timer();
    public Thread runThread;
    public static JFrame f = new JFrame("Snake Game");
    public JPanel firstpage = new JPanel();
    public JPanel secondpage = new JPanel();
    public JLabel FPS_value = new JLabel(Integer.toString(fps));

    JLabel name = new JLabel("Name: Bingxia Zong");
    JLabel userid = new JLabel("Userid: 20329184");
    JLabel description = new JLabel("Snake Game Description");
    JLabel line1 = new JLabel("Use keyboard to control the game:");
    JLabel line2 = new JLabel("ENTER OR click 'Play Game' button:   enter snake game.");
    JLabel line3 = new JLabel("S:   start a new game");
    JLabel line4 = new JLabel("P:   pause the current game and continue the paused game");
    JLabel line5 = new JLabel("R:   restart the game with previous speed and FPS");
    JLabel line6 = new JLabel("      After restarting, press 'S' to start the new game");
    JLabel line7 = new JLabel("U:   speed up at any time");
    JLabel line8 = new JLabel("D:   slow down at any time");
    JLabel line9 = new JLabel("Arrow keys:   change direction");


    // START HERE
    public static void main(String args[]) {

        SnakeGame snakegame = new SnakeGame();
        if (args.length >= 1){
            snakegame.fps = Integer.parseInt(args[0]);
            snakegame.FPS_value = new JLabel(Integer.toString(snakegame.fps));
        }

        if (args.length >= 2){
            snakegame.level = Integer.parseInt(args[1]);
            snakegame.speed = 500 - (Integer.parseInt(args[1]) - 1) * 50;
        }

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(windowwidth, windowheight);
        f.setResizable(false);

        snakegame.FirstPage();
        snakegame.repaint();
        f.setVisible(true);
    }


    public JButton akibutton (String name) {
        JButton test = new JButton(name);
        test.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    f.remove(firstpage);
                    SnakeView snakegrid = new SnakeView();
                    f.add(secondpage);

                    secondpage.setLayout(null);
                    secondpage.add(snakegrid);
                    snakegrid.requestFocus();
                    snakegrid.setBounds(0,0,800,600);
                    f.setVisible(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        return test;
    }
    public JButton playgame = akibutton("Play Game");


    public void FirstPage() {
        firstpage.setLayout(null);
        firstpage.add(description);
        description.setBounds(300,20,620,20);
        firstpage.add(line1);
        line1.setBounds(180,40,620,40);
        firstpage.add(line2);
        line2.setBounds(180,60,620,60);
        firstpage.add(line3);
        line3.setBounds(180,75,620,75);
        firstpage.add(line4);
        line4.setBounds(180,90,620,90);
        firstpage.add(line5);
        line5.setBounds(180,105,620,105);
        firstpage.add(line6);
        line6.setBounds(180,120,620,120);
        firstpage.add(line7);
        line7.setBounds(180,135,620,135);
        firstpage.add(line8);
        line8.setBounds(180,150,620,150);
        firstpage.add(line9);
        line9.setBounds(180,165,620,165);
        firstpage.add(name);
        name.setBounds(50,270,200,250);
        firstpage.add(userid);
        userid.setBounds(50,285,200,265);
        firstpage.add(playgame);
        playgame.setBounds(300, 300, 100, 60);
        f.add(firstpage);
        f.requestFocus();

        playgame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                f.remove(firstpage);
                SnakeView snakegrid = new SnakeView();
                f.add(secondpage);
                secondpage.setLayout(null);
                secondpage.add(snakegrid);
                snakegrid.setBounds(0,0,800,600);
                snakegrid.requestFocus();
                f.setVisible(true);
            }
        });
    }


    public void LevelUp () {
        if (level < 10) {
            level++;
            speed = speed-50;
        }
    }

    public void LevelDown () {
        if (level > 1) {
            level--;
            speed = speed + 50;
        }
    }

    public class SnakeView extends Canvas implements Runnable, KeyListener {
        private long lastPressProcessed = 0;

        public synchronized void paint(Graphics g) {
            this.setPreferredSize(new Dimension(windowwidth, windowheight));
            this.addKeyListener(this);
            if (snake == null) {
                snake = new LinkedList<Point>();
                GenerateDefaultSnake();
                PlaceFruit();
            }

            if (runThread == null) {
                runThread = new Thread(this);
                runThread.start();
            }

            DrawGrid(g);
            DrawSnake(g);
            DrawFruit(g);
            DrawScore(g);
            DrawSpeed(g);
            DrawFPS(g);
        }

        public void GenerateDefaultSnake() {
            score = 0;
            started = false;
            opposite_ing = false;
            snake.clear();
            snake.add(new Point(0, 1));
            snake.add(new Point(0, 0));
            direction = Direction.nodirection;
        }


        public void Move() {
            if (paused) {
                return;
            }
            Point head = snake.peekFirst();
            Point newPoint = head;
            switch (direction) {
                case Direction.up:
                    newPoint = new Point(head.x, head.y - 1);
                    break;
                case Direction.down:
                    newPoint = new Point(head.x, head.y + 1);
                    break;
                case Direction.left:
                    newPoint = new Point(head.x - 1, head.y);
                    break;
                case Direction.right:
                    newPoint = new Point(head.x + 1, head.y);
                    break;
            }
            // when we move ahead for one step, we need to remove one tail
            snake.remove(snake.peekLast());

            if (newPoint.equals(fruit)) {
                // the snake has hit fruit
                score = score + level;
                Point addPoint = (Point) newPoint.clone();

                if(!opposite && !loselength){
                    opposite_ing = false;
                }else if(loselength){
                    if (snake.peekLast() != snake.peekFirst()) snake.remove(snake.peekLast());
                    if (snake.peekLast() != snake.peekFirst()) snake.remove(snake.peekLast());
                    if (snake.peekLast() != snake.peekFirst()) snake.remove(snake.peekLast());
                    opposite_ing = false;
                }else if(opposite){
                    opposite_ing = true;
                }

                switch (direction) {
                    case Direction.up:
                        newPoint = new Point(head.x, head.y - 1);
                        break;
                    case Direction.down:
                        newPoint = new Point(head.x, head.y + 1);
                        break;
                    case Direction.left:
                        newPoint = new Point(head.x - 1, head.y);
                        break;
                    case Direction.right:
                        newPoint = new Point(head.x + 1, head.y);
                        break;
                }
                snake.push(addPoint);
                PlaceFruit();
            } else if (newPoint.x < 0 || newPoint.x > (gridwidth - 1)) {
                // we went out of bound, reset game
                GenerateDefaultSnake();
                return;
            } else if (newPoint.y < 0 || newPoint.y > (gridheight - 1)) {
                // we went out of bound, reset game
                GenerateDefaultSnake();
                return;
            } else if (snake.contains(newPoint)) {
                // we ran into ourselves, reset game
                GenerateDefaultSnake();
                return;
            }
            // if we reach this point in code, we are still good
            snake.push(newPoint);

        }

        public void DrawScore(Graphics g) {
            g.drawString("Score: " + score, 150, boxheight * gridheight + 20);
        }

        public void DrawSpeed(Graphics g) {
            g.drawString("Speed: " + level, 30, boxheight * gridheight + 20);
        }

        public void DrawFPS(Graphics g) {
            g.drawString("FPS: " + fps, 30, boxheight * gridheight + 40);
        }

        // draw grid
        public void DrawGrid(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, gridwidth * boxwidth, gridheight * boxheight);
            // draw vertical lines
            for (int i = 0; i < gridwidth * boxwidth; i += boxwidth) {
                g.drawLine(i, 0, i, boxheight * gridheight);
            }
            // draw horizontal lines
            for (int i = 0; i < gridheight * boxheight; i += boxheight) {
                g.drawLine(0, i, gridwidth * boxwidth, i);
            }
        }

        public void DrawSnake(Graphics g) {
            g.setColor(Color.PINK);
            for (Point p : snake) {
                g.fillRect(p.x * boxwidth, p.y * boxheight, boxwidth, boxheight);
            }
            g.setColor(Color.BLACK);
        }

        public void DrawFruit(Graphics g) {
            if (loselength) g.setColor(Color.RED);
            else if (opposite) g.setColor(Color.GREEN);
            else g.setColor(Color.YELLOW);
            g.fillOval(fruit.x * boxwidth, fruit.y * boxheight, boxwidth, boxheight);
            g.setColor(Color.BLACK);
        }

        public void PlaceFruit() {
            Random rand = new Random();
            int randomX = rand.nextInt(gridwidth);
            int randomY = rand.nextInt(gridheight);
            Point randomPoint = new Point(randomX, randomY);
            while (snake.contains(randomPoint)) {
                randomX = rand.nextInt(gridwidth);
                randomY = rand.nextInt(gridheight);
                randomPoint = new Point(randomX, randomY);
            }
            fruit = randomPoint;
            int tmp = (int )(Math.random() * 50 + 1);
            if (tmp > 20){
                opposite = false;
                loselength = false;
            }else if (tmp > 10){
                opposite = true;
                loselength = false;
            }else{
                opposite = false;
                loselength = true;
            }
        }

        public class MyTimer_repaint extends TimerTask {
            public void run() {
                repaint();
            }
        }

        @Override
        public void run() {
            timer_repaint.schedule(new MyTimer_repaint(), 0, 1000 / fps);
            while (true) {
                Move();
                try {
                    Thread.currentThread();
                    Thread.sleep(speed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_S:
                    if (started == false) {
                        GenerateDefaultSnake();
                        started = true;
                        direction = Direction.down;
                    }
                    // go to the opposite directions does not work
                case KeyEvent.VK_UP:
                    if ((started == true) && (direction != Direction.down) && (direction != Direction.up)) {
                        if (opposite_ing) direction = Direction.down;
                        else direction = Direction.up;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if ((started == true) && (direction != Direction.up) && (direction != Direction.down)) {
                        if (opposite_ing) direction = Direction.up;
                        else direction = Direction.down;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if ((started == true) && (direction != Direction.left) && (direction != Direction.right)) {
                        if (opposite_ing) direction = Direction.left;
                        else direction = Direction.right;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if ((started == true) && (direction != Direction.right) && (direction != Direction.left)) {
                        if (opposite_ing) direction = Direction.right;
                        else direction = Direction.left;
                    }
                    break;
                case KeyEvent.VK_R:
                    GenerateDefaultSnake();
                    break;

                case KeyEvent.VK_P:
                    if (System.currentTimeMillis() - lastPressProcessed > 500) {
                        paused = !paused;
                        lastPressProcessed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_U:
                    if (System.currentTimeMillis() - lastPressProcessed > 500) {
                        LevelUp();
                        lastPressProcessed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_D:
                    if (System.currentTimeMillis() - lastPressProcessed > 500) {
                        LevelDown();
                        lastPressProcessed = System.currentTimeMillis();
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

}