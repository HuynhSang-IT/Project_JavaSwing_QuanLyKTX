package com.quanlyktx.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RaisedButton extends JButton {

    private Color shadowColor = new Color(0, 0, 0, 50); // Màu bóng (đen mờ)
    private Color hoverColor;
    private Color pressedColor;
    private Color originalColor;
    private boolean isHover = false;
    private boolean isPressed = false;
    private int cornerRadius = 10; // Độ bo tròn
    private int shadowSize = 3;    // Độ dày bóng

    public RaisedButton(String text) {
        super(text);
        setContentAreaFilled(false); // Tắt nền mặc định
        setFocusPainted(false);      // Tắt viền focus
        setBorderPainted(false);     // Tắt viền đen
        setOpaque(false);            // Để vẽ được bóng trong suốt
        
        // Padding để chữ không bị sát lề và chừa chỗ cho bóng
        setBorder(new EmptyBorder(10, 20, 10 + shadowSize, 20)); 
        
        // Font mặc định
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm hiệu ứng chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHover = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.originalColor = bg;
        // Tự động tính màu hover (sáng hơn) và pressed (tối hơn)
        this.hoverColor = brighten(bg, 0.15); // Sáng lên 15%
        this.pressedColor = darken(bg, 0.1);  // Tối đi 10%
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int sh = isPressed ? 1 : shadowSize; // Khi nhấn thì bóng nhỏ lại (cảm giác lún xuống)
        int yOffset = isPressed ? 2 : 0;     // Khi nhấn thì nút dịch xuống

        // 1. Vẽ bóng (Shadow)
        if (!isPressed) {
            g2.setColor(shadowColor);
            g2.fill(new RoundRectangle2D.Float(2, 2 + yOffset, width - 4, height - 4 - shadowSize, cornerRadius, cornerRadius));
        }

        // 2. Chọn màu nền dựa trên trạng thái
        if (isPressed) g2.setColor(pressedColor);
        else if (isHover) g2.setColor(hoverColor);
        else g2.setColor(originalColor != null ? originalColor : getBackground());

        // 3. Vẽ nút (Button Body)
        // Trừ đi shadowSize ở chiều cao để chừa chỗ cho bóng
        g2.fill(new RoundRectangle2D.Float(0, yOffset, width, height - shadowSize, cornerRadius, cornerRadius));

        g2.dispose();

        // 4. Vẽ chữ (Text) - Dịch chuyển chữ khi nhấn
        super.paintComponent(g);
    }
    
    // Hàm phụ trợ làm sáng màu
    private Color brighten(Color color, double fraction) {
        int r = Math.min(255, (int) (color.getRed() + (255 * fraction)));
        int g = Math.min(255, (int) (color.getGreen() + (255 * fraction)));
        int b = Math.min(255, (int) (color.getBlue() + (255 * fraction)));
        return new Color(r, g, b);
    }

    // Hàm phụ trợ làm tối màu
    private Color darken(Color color, double fraction) {
        int r = Math.max(0, (int) (color.getRed() * (1 - fraction)));
        int g = Math.max(0, (int) (color.getGreen() * (1 - fraction)));
        int b = Math.max(0, (int) (color.getBlue() * (1 - fraction)));
        return new Color(r, g, b);
    }
    
    // Override để chỉnh vị trí chữ khi nhấn nút (tạo cảm giác 3D thật hơn)
    @Override 
    public Insets getInsets() {
        Insets i = super.getInsets();
        if (isPressed) {
            return new Insets(i.top + 2, i.left, i.bottom - 2, i.right);
        }
        return i;
    }
}