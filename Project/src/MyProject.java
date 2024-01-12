import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MyProject extends JFrame {
	RoundedButton gameStart, exitGame, gameRules; // 게임 시작, 게임 종료, 게임 방법 버튼
	RoundedButton turnStart; // 게임 시작 화면으로 돌아가는 버튼
	JFrame Initial_display; // 게임 시작 화면 JFrame
	JFrame difficulty_display; // 게임 난이도 선택 화면 JFrame
	JFrame game_display; // 게임 진행 화면 JFrame
	JFrame rank_display; // 게임 ranking 화면
	JFrame loading_display; // 게임 로딩되는 화면 JFrame
	JPanel gameControl; // 게임 시작, 게임 종료, 게임 방법 버튼을 놓을 JPanel
	JPanel gameImage; // 게임 이미지를 나타낼 JPanel
	JPanel pengsoo; // 카드 게임에 사용될 카드를 놓을 JPanel
	JButton[] pengsooImage; // 카드 게임에 사용될 카드를 나타낼 JButton
	CircleButton easy, normal, hard; // 게임 난이도 쉬움, 보통, 어려움 선택 버튼
	JButton[][] FrontCards; // 카드 앞면 JButton
	JButton[][] Cards; // 카드 JButton
	int whatDifficulty; // 1이면 난이도 easy, 2이면 난이도 normal, 3이면 난이도 hard
	List<Integer> easyRank = new ArrayList<Integer>();
	List<Integer> normalRank = new ArrayList<Integer>();
	List<Integer> hardRank = new ArrayList<Integer>();
	String level; // 현재 무슨 난이도인지 나타내는 String

	public MyProject() {
		Initial_display = new JFrame();
		Container c = Initial_display.getContentPane();
		// c.setBackground(new Color(251, 255, 185));

		Initial_display.setLayout(new BorderLayout());
		Initial_display.setSize(1000, 800);
		Initial_display.setTitle("Card Flip Game");
		Initial_display.setResizable(false);
		Initial_display.setLocationRelativeTo(null);
		Initial_display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 게임 이름과 게임 이미지 삽입
		JLabel title = new JLabel("Card Flip Game");
		title.setFont(new Font("나눔스퀘어_ac", Font.BOLD, 50));
		title.setForeground(Color.BLACK);
		title.setLocation(180, 200);
		title.setSize(500, 50);

		JLabel subTitle = new JLabel("(카드 뒤집기 게임)");
		subTitle.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
		subTitle.setForeground(Color.BLACK);
		subTitle.setLocation(260, 250);
		subTitle.setSize(500, 50);

		ImageIcon image = new ImageIcon("image/펭하.jpg");
		Image img = image.getImage();
		Image change = img.getScaledInstance(1000, 800, Image.SCALE_SMOOTH);
		JLabel backImg = new JLabel(new ImageIcon(change));
		backImg.setSize(1000, 800);
		c.add(backImg);

		gameStart = new RoundedButton("게임 시작", Color.ORANGE);
		exitGame = new RoundedButton("게임 종료", Color.ORANGE);
		gameRules = new RoundedButton("게임 방법", Color.ORANGE);
		gameStart.setSize(200, 100);
		gameStart.setLocation(20, 600);
		gameRules.setSize(200, 100);
		gameRules.setLocation(240, 600);
		exitGame.setSize(200, 100);
		exitGame.setLocation(460, 600);

		gameStart.addActionListener(new gameButtonAction());
		exitGame.addActionListener(new gameButtonAction());
		gameRules.addActionListener(new gameButtonAction());

		JPanel p = (JPanel) Initial_display.getGlassPane();
		p.setLayout(null);
		p.add(title);
		p.add(subTitle);
		p.add(gameStart);
		p.add(gameRules);
		p.add(exitGame);
		p.setVisible(true);
		Initial_display.setGlassPane(p);

		Initial_display.setVisible(true);
	}

	private class gameButtonAction implements ActionListener { // gameStart, exitGame, gameRules JButton 이벤트 처리
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b == gameStart) {
				Thread setDifficultyThread = new Thread(new SetDifficulty());
				setDifficultyThread.start();
			} else if (b == exitGame) {
				int result = JOptionPane.showConfirmDialog(null, "정말 게임을 종료하시겠습니까?", "Exit Game",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Thread exitGameThread = new Thread(new ExitGame());
					exitGameThread.start();
				}
			} else if (b == gameRules) {
				Thread gameRulesThread = new Thread(new GameRules());
				gameRulesThread.start();
			}
		}
	}

	class SetDifficulty extends JFrame implements Runnable { // 난이도 설정하는 클래스
		JLabel label; // 게임 난이도를 선택하라는 메시지를 놓을 JLabel

		public SetDifficulty() {
			difficulty_display = new JFrame();
			difficulty_display.setTitle("Set Game Difficulty");
			difficulty_display.setSize(400, 300);
			difficulty_display.setLocationRelativeTo(null);
			difficulty_display.setResizable(false);

			ImageIcon image = new ImageIcon("image/게임난이도.png");
			Image img = image.getImage();
			Image change = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
			JLabel backImg = new JLabel(new ImageIcon(change));
			backImg.setOpaque(true);
			backImg.setSize(400, 300);
			difficulty_display.add(backImg);

			label = new JLabel("난이도를 선택하세요!", SwingConstants.CENTER);
			label.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 18));
			label.setLocation(40, 50);
			label.setSize(300, 30);

			easy = new CircleButton("Easy", Color.PINK);
			easy.setLocation(13, 120);
			easy.setSize(100, 100);
			easy.addActionListener(new difficultyButtonAction());

			normal = new CircleButton("Normal", Color.PINK);
			normal.setLocation(143, 120);
			normal.setSize(100, 100);
			normal.addActionListener(new difficultyButtonAction());

			hard = new CircleButton("Hard", Color.PINK);
			hard.setLocation(273, 120);
			hard.setSize(100, 100);
			hard.addActionListener(new difficultyButtonAction());

			JPanel p = (JPanel) difficulty_display.getGlassPane();
			p.setLayout(null);
			p.add(label);
			p.add(easy);
			p.add(normal);
			p.add(hard);
			p.setVisible(true);
			difficulty_display.setGlassPane(p);

			difficulty_display.dispose(); // 현재 창만 닫히도록
		}

		@Override
		public void run() {
			difficulty_display.setVisible(true);
		}
	}

	class Loading extends JFrame { // 게임 Loading하는 화면 설정하는 클래스
		public Loading() {
			loading_display = new JFrame();
			loading_display.setTitle("Game Loading ...");
			loading_display.setSize(500, 400);
			loading_display.setLocationRelativeTo(null);
			loading_display.setResizable(false);

			ImageIcon image = new ImageIcon("image/펭수로딩.jpg");
			Image img = image.getImage();
			Image change = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
			JLabel backImg = new JLabel(new ImageIcon(change));
			backImg.setOpaque(true);
			backImg.setSize(500, 400);
			loading_display.add(backImg);

			JLabel label = new JLabel("로딩중ㆍㆍㆍ", SwingConstants.CENTER);
			label.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
			label.setLocation(40, 150);
			label.setSize(300, 30);

			JPanel p = (JPanel) loading_display.getGlassPane();
			p.setLayout(null);
			p.add(label);
			p.setVisible(true);
			loading_display.setGlassPane(p);

			loading_display.dispose();
		}
	}

	private class difficultyButtonAction implements ActionListener { // easy, normal, hard JButton 이벤트 처리
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b == easy) {
				whatDifficulty = 1;
				level = "Easy";
			} else if (b == normal) {
				whatDifficulty = 2;
				level = "Normal";
			} else if (b == hard) {
				whatDifficulty = 3;
				level = "Hard";
			}
			new Loading();
			difficulty_display.setVisible(false);
			Initial_display.setVisible(false);
			loading_display.setVisible(true);
			Thread game = new Thread(new Difficulty());
			game.start();
		}
	}

	class Difficulty extends JFrame implements Runnable { // 난이도 별 게임을 실행시키는 클래스
		JLabel time1, time2, timeInter; // 남은 시간 분을 나타내는 time1, 남은 시간 초를 나타내는 time2, 분과 초 사이에 ':'를 나타낼 timeInter JLabel
		int timer; // 남은 시간을 나타내는 timer
		JLabel difficulty, difficultyInfor; // 현재 게임 난이도를 나타낼 difficulty, 난이도라는 글자를 쓰기 위한 difficulty JLabel
		int col; // 난이도에 따른 카드 열의 개수를 나타낼 col
		JLabel scoreInfo; // 점수라는 글을 쓰기 위한 scoreInfo JLabel
		int score; // 현재 얻은 점수를 나타내는 score
		int flipNumber, flipSuccess, flipFail; // 카드는 한 번에 두 개씩 뒤집혀야 하므로, 뒤집힌 횟수를 나타낼 flipNumber, 짝이 맞춰진 경우 성공 횟수를 나타낼
												// flipSuccess, 짝 맞추기 실패한 경우 실패 횟수를 나타낼 flipFail
		int firstX, firstY; // 첫 번째로 뒤집힌 카드의 행과 열을 나타내기 위한 firsX, firstY
		boolean isSuccess; // 제한 시간 내 모든 카드 짝을 다 맞추었는지 여부를 나타내기 위한 isSuccess
		boolean isFail; // 카드 짝 맞추기 실패 여부를 나타내기 위한 isFail
		ImageIcon image; // 카드 뒷면 이미지를 나타낼 ImageIcon
		Image img, change; // 카드 사이즈를 JButton 크기에 맞게 조절하기 위한 image
		String[][] cardImage; // 뒤섞인 모든 카드에 삽입될 카드 앞면 이미지 파일 문자열을 저장하기 위한 String 2차원 배열
		int maxImgRow, maxImgCol; // 게임 난이도에 따른 카드 이미지 크기를 나타낼 maxImgRow, maxImgCol->난이도가 높아짐에 따라 카드 갯수가 많아지는데, 배경
									// 크기는 같기 때문에 크기를 작게 조절해야줘야함
		boolean[][] isFlipSuccessed; // 카드의 짝이 맞아 이미 해당 카드가 뒤집혔는 지 확인하기 위한 boolean형 2차원 배열->이미 짝이 맞아 뒤집힌 카드가 다시 선택되어
										// 뒤집히는 것을 막기 위한 것
		boolean[][] isFliped; // 카드가 이미 뒤집혔는 지 확인하기 위한 boolean형 2차원 배열->이미 선택한 카드가 다시 선택되어 뒤집히는 것을 막기 위한 것
		RoundedButton hintButton; // 0.5초동안 카드의 앞면을 볼 수 있는 힌트를 제공하는 RoundedButton
		int hintNum; // 힌트를 얻을 수 있는 횟수를 나타냄

		public Difficulty() {
			JOptionPane.showMessageDialog(null, "난이도 " + level + " 선택!", "Card Flip Game Difficulty",
					JOptionPane.INFORMATION_MESSAGE);
			if (whatDifficulty == 1) {
				hintNum = 0;
				col = 4;
				maxImgRow = 150;
				maxImgCol = 250;
			} else if (whatDifficulty == 2) {
				hintNum = 1;
				col = 8;
				maxImgRow = 150;
				maxImgCol = 250;
			} else if (whatDifficulty == 3) {
				hintNum = 3;
				col = 16;
				maxImgRow = 100;
				maxImgCol = 200;
			}
			game_display = new JFrame();
			game_display.setLayout(new BorderLayout());
			Container c = game_display.getContentPane();
			c.setLayout(new BorderLayout());
			game_display.setTitle("Difficulty " + level + " Card Flip Game");
			game_display.setSize(2000, 800);
			game_display.setResizable(false);
			game_display.setLocationRelativeTo(null);
			game_display.dispose();

			JPanel infor1 = new JPanel(new GridLayout(1, 2));

			JPanel timePanel = new JPanel();
			JLabel timeInfor = new JLabel("Time ");
			timeInfor.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			timeInfor.setForeground(Color.BLACK);
			timePanel.add(timeInfor);
			time1 = new JLabel("01 ");
			time1.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			time1.setForeground(Color.PINK);
			timePanel.add(time1);
			timeInter = new JLabel(": ");
			timeInter.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			timeInter.setForeground(Color.PINK);
			timePanel.add(timeInter);
			time2 = new JLabel("00 ");
			time2.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			time2.setForeground(Color.PINK);
			timePanel.add(time2);

			JPanel difficultyPanel = new JPanel();
			difficultyInfor = new JLabel("난이도 ");
			difficultyInfor.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			difficultyInfor.setForeground(Color.BLACK);
			difficultyPanel.add(difficultyInfor);
			difficulty = new JLabel(level + " ");
			difficulty.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 25));
			difficulty.setForeground(Color.PINK);
			difficultyPanel.add(difficulty);

			infor1.add(timePanel);
			infor1.add(difficultyPanel);

			JPanel infor2 = new JPanel(new GridLayout(1, 3));

			JLabel blank = new JLabel();
			score = 0;
			JPanel scorePanel = new JPanel();
			JLabel showScore = new JLabel("점수: ");
			showScore.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
			showScore.setForeground(Color.BLACK);
			scorePanel.add(showScore);
			scoreInfo = new JLabel();
			scoreInfo.setText(score + " ");
			scoreInfo.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
			scoreInfo.setForeground(Color.PINK);
			scorePanel.add(scoreInfo);

			JPanel hintPanel = new JPanel();
			hintButton = new RoundedButton("HINT", Color.PINK);
			hintButton.addActionListener(new HintAction());
			hintPanel.add(hintButton);

			infor2.add(blank);
			infor2.add(scorePanel);
			infor2.add(hintPanel);

			JPanel panel = new JPanel(new GridLayout(2, col, 10, 10));

			Cards = new JButton[2][col];
			image = new ImageIcon("image/펭수카드뒷면.jpg");
			img = image.getImage();
			change = img.getScaledInstance(maxImgRow, maxImgCol, Image.SCALE_SMOOTH);
			isFlipSuccessed = new boolean[2][col];
			isFliped = new boolean[2][col];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < col; j++) {
					Cards[i][j] = new JButton(new ImageIcon(change));
					Cards[i][j].setSize(maxImgRow, maxImgCol);
					Cards[i][j].setContentAreaFilled(false);
					Cards[i][j].setBorderPainted(false);
					Cards[i][j].setFocusPainted(false);
					Cards[i][j].addActionListener(new CardFlipAction(i, j));
					panel.add(Cards[i][j]);
					isFlipSuccessed[i][j] = false;
					isFliped[i][j] = false;
				}
			}

			FrontCards = new JButton[2][col];
			// 카드 랜덤으로 설정하기
			HashSet<Integer> set = new HashSet<Integer>();
			Random ran = new Random();
			int n;
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < col; i++) {
				do {
					n = (int) (Math.random() * 16.0 + 1.0);
				} while (set.contains(n));
				set.add(n);
				list.add(n);
			}
			ArrayList<Integer> numList = new ArrayList<Integer>();
			Iterator<Integer> iterator_one = list.iterator();
			while (iterator_one.hasNext())
				numList.add(iterator_one.next());
			Iterator<Integer> iterator_two = list.iterator();
			while (iterator_two.hasNext())
				numList.add(iterator_two.next());
			Collections.shuffle(numList);
			int rIndex = 0, cIndex = 0;
			cardImage = new String[2][col];
			Iterator<Integer> iterator = numList.iterator();
			while (iterator.hasNext()) {
				if (cIndex == col) {
					rIndex = 1;
					cIndex = 0;
				}
				String name = "image/펭수" + iterator.next() + ".png";
				cardImage[rIndex][cIndex] = name;
				cIndex++;
			}

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < col; j++) {
					ImageIcon image2 = new ImageIcon(cardImage[i][j]);
					Image img2 = image2.getImage();
					Image change2 = img2.getScaledInstance(maxImgRow, maxImgCol, Image.SCALE_SMOOTH);
					FrontCards[i][j] = new JButton(new ImageIcon(change2));
					FrontCards[i][j].setSize(maxImgRow, maxImgCol);
					FrontCards[i][j].setContentAreaFilled(false);
					FrontCards[i][j].setBorderPainted(false);
					FrontCards[i][j].setFocusPainted(false);
				}
			}

			flipNumber = 0;
			flipSuccess = 0;
			flipFail = 0;
			isSuccess = false;
			isFail = false;

			c.add(infor1, BorderLayout.NORTH);
			c.add(panel, BorderLayout.CENTER);
			c.add(infor2, BorderLayout.SOUTH);

			loading_display.setVisible(false);
		}

		@Override
		public void run() {
			game_display.setVisible(true);
			for (timer = 59; timer >= 0; timer--) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timer < 60 && timer >= 10) {
					time1.setText("00 ");
					time2.setText(timer + " ");
				} else if (timer >= 0) {
					time1.setText("00 ");
					time2.setText("0" + timer + " ");
				}
				if (isSuccess) {
					JOptionPane.showMessageDialog(null, "짝 맞추기 성공!", "Card Flip Success",
							JOptionPane.INFORMATION_MESSAGE);
					if (level.equals("Easy"))
						makeRanking(easyRank, score);
					else if (level.equals("Normal"))
						makeRanking(normalRank, score);
					else if (level.equals("Hard"))
						makeRanking(hardRank, score);
					JOptionPane.showMessageDialog(null, "점수: " + score + "점!\nCard Flip Game 종료!",
							"End Of Card Flip Game", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}
			if (!isSuccess && timer < 0 && flipSuccess != col) {
				JOptionPane.showMessageDialog(null, "Time Over!\nCard Flip Game 종료!", "Card Flip Game Time Over",
						JOptionPane.INFORMATION_MESSAGE);
				score = -1;
			}
			Thread rank = new Thread(new GameRank(score));
			rank.start();
		}

		public void makeRanking(List<Integer> ranking, int rScore) {
			if (!ranking.contains(rScore)) {
				ranking.add(rScore);
				Collections.sort(ranking, Collections.reverseOrder());
			}
		}

		class HintAction implements ActionListener { // Hint RoundedButton이 눌려졌을 때 발생할 이벤트
			@Override
			public void actionPerformed(ActionEvent e) {
				if (level.equals("Easy")) {
					JOptionPane.showMessageDialog(null, "Easy Mode에서는 Hint 사용 불가!", "Hint Extinction",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (hintNum > 0) { // 힌트를 얻을 수 있는 횟수가 아직 남아있을 경우
					hintNum--;
					for (int i = 0; i < 2; i++)
						for (int j = 0; j < col; j++) {
							if (!isFlipSuccessed[i][j])
								Cards[i][j].setIcon(FrontCards[i][j].getIcon()); // 카드 앞면을 보여줌
						}
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							for (int i = 0; i < 2; i++)
								for (int j = 0; j < col; j++) {
									if (!isFlipSuccessed[i][j])
										Cards[i][j].setIcon(new ImageIcon(change));
								}
						}
					};
					timer.schedule(task, 300); // 0.3초 뒤 다시 카드 뒷면으로 뒤집음
				} else { // 힌트를 얻을 수 있는 횟수가 남아있지 않을 경우
					JOptionPane.showMessageDialog(null, "Hint를 다 사용하셨습니다!", "Hint Extinction",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		private class CardFlipAction implements ActionListener { // 카드가 뒤집혔을 때(카드 버튼이 눌려졌을 때) 발생하는 이벤트
			int x, y;

			public CardFlipAction(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (flipNumber == 2 || isFlipSuccessed[x][y] || isFliped[x][y]) { // 이미 카드를 두 번 뒤집었거나, 짝이 맞게 이미 뒤집힌 카드를
																					// 선택하거나, 이미 선택되어 뒤집힌 카드를 다시 선택했을 경우
					return; // 아무 이벤트도 발생하지 않도록 함
				}
				JButton button = (JButton) e.getSource();
				button.setIcon(FrontCards[x][y].getIcon());
				isFliped[x][y] = true;
				flipNumber++;
				if (flipNumber == 1) {
					firstX = x;
					firstY = y;
				} else if (flipNumber == 2) {
					if (cardImage[firstX][firstY].equals(cardImage[x][y])) { // 같은 모양의 카드를 뒤집은 경우
						flipSuccess++;
						isFlipSuccessed[firstX][firstY] = true;
						isFlipSuccessed[x][y] = true;
						if (!isFail) // 전 과정에서 카드 짝 맞추기를 성공한 경우
							score += 2; // 2점을 획득함
						else // 전 과정에서 카드 짝 맞추기를 실패한 경우
							score += 1; // 1점을 획득함
						isFail = false;
						scoreInfo.setText(score + " ");
						if (flipSuccess == col) {
							isSuccess = true;
						}
					} else { // 다른 모양의 카드를 뒤집은 경우
						isFail = true; // 카드 짝 맞추기를 실패했다는 것을 표시함
						--score;
						isFliped[firstX][firstY] = false;
						isFliped[x][y] = false;
						if (score < 0)
							score = 0;
						scoreInfo.setText(score + " ");
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								Cards[firstX][firstY].setIcon(new ImageIcon(change));
								Cards[x][y].setIcon(new ImageIcon(change));
							}
						};
						timer.schedule(task, 500);
						flipNumber = 0;
					}
					flipNumber = 0;
				}
			}
		}
	}

	class GameRank extends JFrame implements Runnable { // 난이도 별 ranking을 나타내는 클래스
		RoundedButton backInitGame;
		JLabel[] gradeScore;

		public GameRank(int score) {
			rank_display = new JFrame();
			rank_display.setTitle("Game Ranking");
			rank_display.setSize(700, 700);
			rank_display.setLocationRelativeTo(null);
			rank_display.setResizable(false);
			ImageIcon image = new ImageIcon("image/게임순위.jpg");
			Image img = image.getImage();
			Image change = img.getScaledInstance(700, 700, Image.SCALE_SMOOTH);
			JLabel backImg = new JLabel(new ImageIcon(change));
			backImg.setOpaque(true);
			backImg.setSize(700, 700);
			rank_display.add(backImg);

			backInitGame = new RoundedButton("처음으로 돌아가기", (new Color(255, 136, 104)));
			backInitGame.setSize(200, 70);
			backInitGame.setLocation(450, 550);
			backInitGame.addActionListener(new BackAction());

			JLabel difficultyRank1 = new JLabel("난이도 ");
			difficultyRank1.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
			difficultyRank1.setForeground(Color.BLACK);
			difficultyRank1.setLocation(230, 10);
			difficultyRank1.setSize(60, 50);
			JLabel difficultyRank2 = new JLabel(level + ' ');
			difficultyRank2.setFont(new Font("나눔스퀘어_ac", Font.BOLD, 20));
			difficultyRank2.setForeground((new Color(255, 136, 104)));
			difficultyRank2.setSize(80, 50);
			JLabel difficultyRank3 = new JLabel(" Ranking ");
			difficultyRank3.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
			difficultyRank3.setForeground(Color.BLACK);
			difficultyRank3.setLocation(380, 10);
			difficultyRank3.setSize(100, 50);

			gradeScore = new JLabel[10];
			int easyIndex = 0;
			int normalIndex = 0;
			int hardIndex = 0;
			int index = 0;
			if (level.equals("Easy")) {
				difficultyRank2.setLocation(310, 10);
				for (Integer in : easyRank) {
					gradeScore[easyIndex] = new JLabel();
					gradeScore[easyIndex].setText((easyIndex + 1) + "등: " + in + "점");
					gradeScore[easyIndex].setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
					if (in == score)
						gradeScore[easyIndex].setForeground((new Color(255, 136, 104)));
					else
						gradeScore[easyIndex].setForeground(Color.BLACK);
					gradeScore[easyIndex].setLocation(295, 50 + easyIndex * 30);
					gradeScore[easyIndex].setSize(100, 50);
					++easyIndex;
				}
				index = easyIndex;
			} else if (level.equals("Normal")) {
				difficultyRank2.setLocation(300, 10);
				for (Integer in : normalRank) {
					gradeScore[normalIndex] = new JLabel();
					gradeScore[normalIndex].setText((normalIndex + 1) + "등: " + in + "점");
					gradeScore[normalIndex].setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 20));
					if (in == score)
						gradeScore[normalIndex].setForeground((new Color(255, 136, 104)));
					else
						gradeScore[normalIndex].setForeground(Color.BLACK);
					gradeScore[normalIndex].setLocation(295, 50 + normalIndex * 30);
					gradeScore[normalIndex].setSize(100, 50);
					++normalIndex;
				}
				index = normalIndex;
			} else if (level.equals("Hard")) {
				difficultyRank2.setLocation(310, 10);
				for (Integer in : hardRank) {
					gradeScore[hardIndex] = new JLabel();
					gradeScore[hardIndex].setText((hardIndex + 1) + "등: " + in + "점");
					gradeScore[hardIndex].setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 30));
					if (in == score)
						gradeScore[hardIndex].setForeground((new Color(255, 136, 104)));
					else
						gradeScore[hardIndex].setForeground(Color.BLACK);
					gradeScore[hardIndex].setLocation(295, 50 + hardIndex * 30);
					gradeScore[hardIndex].setSize(100, 50);
					++hardIndex;
				}
				index = hardIndex;
			}

			JPanel p = (JPanel) rank_display.getGlassPane();
			for (int i = 0; i < index; i++)
				p.add(gradeScore[i]);
			p.add(difficultyRank1);
			p.add(difficultyRank2);
			p.add(difficultyRank3);
			p.add(backInitGame);
			p.setLayout(null);
			p.setVisible(true);
			rank_display.setGlassPane(p);

			rank_display.dispose();
			game_display.setVisible(false);
		}

		@Override
		public void run() {
			rank_display.setVisible(true);
		}
	}

	class BackAction implements ActionListener { // backInitGame RoundedButton이 눌려졌을 때 발생하는 이벤트
		@Override
		public void actionPerformed(ActionEvent e) {
			rank_display.setVisible(false);
			Initial_display.setVisible(true);
		}
	}

	class GameRules extends JFrame implements Runnable { // 게임 방법을 나타내는 클래스
		JLabel label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12,
				label13; // 그림 및 설명을 넣을
		// JLabel
		RoundedButton exitRules; // 게임 방법 창을 닫는 버튼

		public GameRules() {
			setTitle("Game Rules");
			setSize(700, 630);
			setLocationRelativeTo(null);
			setResizable(false);

			ImageIcon image = new ImageIcon("image/게임방법펭.jpg");
			Image img = image.getImage();
			Image change = img.getScaledInstance(700, 630, Image.SCALE_SMOOTH);
			JLabel backImg = new JLabel(new ImageIcon(change));
			backImg.setOpaque(true);
			backImg.setSize(700, 630);
			add(backImg);

			label1 = new JLabel("ㆍ카드를 뒤집어서 짝을 찾는 게임입니다.");
			label1.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label1.setForeground(Color.BLACK);
			label1.setLocation(10, 30);
			label1.setSize(400, 30);

			label2 = new JLabel("ㆍ");
			label2.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label2.setForeground(Color.BLACK);
			label2.setLocation(10, 150);
			label2.setSize(100, 30);
			image = new ImageIcon("image/펭수카드뒷면.jpg");
			img = image.getImage();
			change = img.getScaledInstance(80, 120, Image.SCALE_SMOOTH);
			JLabel cardImg = new JLabel(new ImageIcon(change));
			cardImg.setSize(80, 120);
			cardImg.setLocation(40, 100);
			label3 = new JLabel(" 를 뒤집어서 같은 카드가 나왔을 때,");
			label3.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label3.setForeground(Color.BLACK);
			label3.setLocation(120, 110);
			label3.setSize(500, 30);

			label4 = new JLabel("      바로 전에 같은 카드가 나온 경우 2점을 획득합니다.");
			label4.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label4.setForeground(Color.BLACK);
			label4.setLocation(120, 130);
			label4.setSize(500, 30);

			label5 = new JLabel("      바로 전에 다른 카드가 나온 경우 1점을 획득합니다.");
			label5.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label5.setForeground(Color.BLACK);
			label5.setLocation(120, 150);
			label5.setSize(500, 30);

			label6 = new JLabel(" 를 뒤집어서 다른 카드가 나오면 1점을 잃습니다.");
			label6.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label6.setForeground(Color.BLACK);
			label6.setLocation(120, 180);
			label6.setSize(500, 30);

			label7 = new JLabel("ㆍ난이도 별로 카드 개수가 달라집니다.");
			label7.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label7.setForeground(Color.BLACK);
			label7.setLocation(10, 250);
			label7.setSize(700, 30);

			label8 = new JLabel("(Easy: 8개, Normal: 16개, Hard: 32개)");
			label8.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 15));
			label8.setForeground(Color.BLACK);
			label8.setLocation(30, 280);
			label8.setSize(500, 20);

			label9 = new JLabel("ㆍ모든 카드의 앞면을 보여주는 힌트가 존재합니다.");
			label9.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label9.setForeground(Color.BLACK);
			label9.setLocation(10, 320);
			label9.setSize(700, 30);

			label10 = new JLabel("ㆍ난이도 별로 힌트를 얻을 수 있는 횟수가 달라집니다.");
			label10.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label10.setForeground(Color.BLACK);
			label10.setLocation(10, 370);
			label10.setSize(700, 30);

			label11 = new JLabel("(Easy: 0번, Normal: 1번, Hard: 3번)");
			label11.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 15));
			label11.setForeground(Color.BLACK);
			label11.setLocation(30, 400);
			label11.setSize(700, 20);

			label12 = new JLabel("ㆍ제한 시간 안에 카드 짝을 다 맞추지 못하면 점수를");
			label12.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label12.setForeground(Color.BLACK);
			label12.setLocation(10, 440);
			label12.setSize(700, 30);

			label13 = new JLabel("얻지 못하고 게임 오버됩니다.");
			label13.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			label13.setForeground(Color.BLACK);
			label13.setLocation(30, 470);
			label13.setSize(700, 30);

			// 게임 방법 창 닫기 위한 exitRules 버튼
			exitRules = new RoundedButton("닫기", Color.WHITE);
			exitRules.setLocation(370, 500);
			exitRules.setSize(100, 60);
			exitRules.addActionListener((e) -> {
				setVisible(false);
			});

			JPanel p = (JPanel) getGlassPane();
			p.setLayout(null);
			p.add(label1);
			p.add(cardImg);
			p.add(label2);
			p.add(label3);
			p.add(label4);
			p.add(label5);
			p.add(label6);
			p.add(label7);
			p.add(label8);
			p.add(label9);
			p.add(label10);
			p.add(label11);
			p.add(label12);
			p.add(label13);
			p.add(exitRules);
			p.setVisible(true);
			setGlassPane(p);

			dispose(); // 현재 창만 닫히도록
		}

		@Override
		public void run() {
			setVisible(true);
		}
	}

	class ExitGame extends JFrame implements Runnable { // 게임 종료하는 클래스
		JLabel label1, label2;
		Container c;

		public ExitGame() {
			c = this.getContentPane();
			setTitle("Exit Game");
			setSize(500, 400);
			setLocationRelativeTo(null);
			setResizable(false);

			ImageIcon image = new ImageIcon("image/펭빠.jpg");
			Image img = image.getImage();
			Image change = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
			JLabel backImg = new JLabel(new ImageIcon(change));
			backImg.setSize(500, 400);
			c.add(backImg);

			// 이미지 위에 글씨 넣기
			label1 = new JLabel("End of Game!!!");
			label1.setFont(new Font("나눔스퀘어_ac", Font.BOLD, 30));
			label1.setForeground(Color.BLACK);
			label1.setLocation(50, 120);
			label1.setSize(220, 30);
			label2 = new JLabel("5초 후 자동 종료됩니다...", SwingConstants.CENTER);
			label2.setFont(new Font("나눔스퀘어_ac", Font.ITALIC, 15));
			label2.setForeground(Color.BLACK);
			label2.setLocation(50, 200);
			label2.setSize(200, 30);
			JPanel p = (JPanel) getGlassPane();
			p.setLayout(null);
			p.add(label1);
			p.add(label2);
			p.setVisible(true);
			setGlassPane(p);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Initial_display.setVisible(false);
		}

		@Override
		public void run() {
			setVisible(true);
			int i;
			for (i = 10; i > 5; i--) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			setVisible(false);
		}
	}

	class CircleButton extends JButton { // 원 모양의 JRadioButton을 만들기 위한 클래스
		Color c;

		public CircleButton(String text, Color c) {
			setText(text);
			setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setOpaque(false);
			this.setPreferredSize(new Dimension(100, 100));
			this.c = c;
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(c);
			g.fillOval(0, 0, this.getSize().width, this.getSize().height);
			super.paintComponent(g);
		}
	}

	class RoundedButton extends JButton { // 모서리가 둥근 JRadioButton을 만들기 위한 클래스
		Color c;

		public RoundedButton(String text, Color c) {
			setText(text);
			setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 20));
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setOpaque(false);
			this.setPreferredSize(new Dimension(200, 100));
			this.c = c;
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(c);
			g.fillRoundRect(0, 0, this.getSize().width - 1, this.getSize().height - 1, 10, 10);
			super.paintComponent(g);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyProject();
			}
		});
	}
}
