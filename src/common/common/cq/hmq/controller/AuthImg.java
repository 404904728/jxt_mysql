package common.cq.hmq.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成验证码的servlet
 * 
 */
public class AuthImg extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7495009875625745124L;
	private Font mFont = new Font("宋体", Font.BOLD, 30);

	public void init() throws ServletException {
		super.init();
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		int width = 72, height = 40; // 60 18
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(new Color(49, 158, 214));
		g.fillRect(0, 0, width, height);
		g.setFont(mFont);
		StringBuilder sRand = new StringBuilder(6);
		List<Color> cList = new ArrayList<Color>();
		cList.add(Color.BLACK);
		cList.add(Color.BLUE);
		cList.add(Color.CYAN);
		cList.add(Color.DARK_GRAY);
		cList.add(Color.GREEN);
		cList.add(Color.MAGENTA);
		cList.add(Color.ORANGE);
		cList.add(Color.PINK);
		cList.add(Color.RED);
		cList.add(Color.WHITE);
		cList.add(Color.YELLOW);
		for (int i = 0; i < 4; i++) {
			String tmp = getRandomChar();
			sRand.append(tmp);
			/*g.setColor(new Color(random.nextInt(255), random.nextInt(255),
					random.nextInt(255)));*/
			g.setColor(cList.get(random.nextInt(11)));
			g.drawString(tmp, 15 * i + 5, 30); // g.drawString(tmp, 15 * i + 5,
												// 15);
		}
		request.getSession().setAttribute("AUTH_IMG_IN_SESSION",
				sRand.toString().toLowerCase());
		g.dispose();
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	private String getRandomChar() {
		String rounmImg = "";
		/*
		 * for (int i = 0; i < 4; i++) { long itmp = Math.round(Math.random() *
		 * 25 + 65); if (itmp % 2 == 0) { rounmImg+=String.valueOf((char) itmp);
		 * } else { rounmImg+=String.valueOf((char) itmp).toLowerCase(); } }
		 */
		rounmImg = String.valueOf((int) (Math.random() * 10));
		return rounmImg;
	}
}
