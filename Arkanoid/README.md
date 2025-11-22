# Trò chơi Arkanoid - Bài tập lớn môn Lập trình hướng đối tượng

## Tác giả

Nhóm 2 - Lớp INT2204_11

1. Nguyễn Duy Hòa - 24021482
2. Lê Trung Hiếu - 24021474
3. Nghiêm Thành Long - 24021554

**Giảng viên hướng dẫn**: Kiều Văn Tuyên
**Học kì**: Học kì 1 - Năm học 2025-2026

---

## Mô tả

Đây là bài tập lớn môn Lập trình hướng đối tượng về game Arkanoid được phát triển bằng ngôn ngữ Java. Bài tập lớn này thể hiện việc thực hiện các nguyên tắc OOP và các mẫu thiết kế

**Tính năng chính:**

1. Game được phát triển sử dụng Java 24+ với GUI JavaFX
2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.
3. Applies multiple design patterns: Singleton, Factory Method, Strategy, Observer, and State.
4. Features multithreading for smooth gameplay and responsive UI.
5. Bao gồm hiệu ứng âm thanh, nhạc nền, hiệu ứng hình ảnh và hệ thống vật phẩm tăng sức mạnh
6. Có hệ thống bảng xếp hạng

**Cơ chế game**

- Điều khiển bảng đỡ bóng để phá hủy các viên gạch
- Thu thập các vật phẩm đặc biệt để tăng khả năng chiến thắng màn chơi
- Có các map khác nhau với độ khó ngẫu nhiên
- Ghi càng nhiều điểm, vượt qua càng nhiều màn để đứng đầu trên bảng xếp hạng

---

## UML Diagram

### Class Diagram

[![Class Diagram](https://github.com/kieuvantuyen01/OOP_demo/raw/master/docs/uml/class-diagram.png)](https://github.com/kieuvantuyen01/OOP_demo/blob/master/docs/uml/class-diagram.png)

_Có thể sử dụng IntelliJ để generate ra Class Diagrams: [https://www.youtube.com/watch?v=yCkTqNxZkbY](https://www.youtube.com/watch?v=yCkTqNxZkbY)_

_Complete UML diagrams are available in the `docs/uml/` folder_

---

## Design Patterns Implementation

_Có dùng hay không và dùng ở đâu_

### 1. Singleton Pattern

[](https://github.com/kieuvantuyen01/OOP_demo#1-singleton-pattern)

**Used in:** `GameManager`, `AudioManager`, `ResourceLoader`

**Purpose:** Ensure only one instance exists throughout the application.

---

## Multithreading Implementation

[](https://github.com/kieuvantuyen01/OOP_demo#multithreading-implementation)

_Có dùng hay không và dùng như thế nào_

The game uses multiple threads to ensure smooth performance:

1. **Game Loop Thread**: Updates game logic at 60 FPS
2. **Rendering Thread**: Handles graphics rendering (EDT for JavaFX Application Thread)
3. **Audio Thread Pool**: Plays sound effects asynchronously
4. **I/O Thread**: Handles save/load operations without blocking UI

---

## Cách cài đặt

1. Clone repository trên Github
2. Mở project trên IDE
3. Cài đặt JDK phiên bản 24, cài đặt JavaFX phiên bản 25
4. Build và chạy chương trình

## Usage

### Controls

|Key|Action|
|---|---|
|`←` or `A`|Move paddle left|
|`→` or `D`|Move paddle right|
|`SPACE`|Launch ball / Shoot laser|
|`P` or `ESC`|Pause game|
|`R`|Restart game|
|`Q`|Quit to menu|

### How to Play

1. **Bắt đầu chơi:** bấm chọn Start ở Menu
2. **Control the paddle**: Use arrow keys or A/D to move left and right.
3. **Launch the ball**: Press SPACE to launch the ball from the paddle.
4. **Phá hủy gạch:** sử dụng bóng va chạm với gạch để phá hủy
5. **Thu thập vật phẩm:** dùng bảng đỡ nhặt các vật phẩm rơi xuống
6. **Không để mất bóng:** không để bóng rơi dưới bảng đỡ
7. **Hoàn thành màn:** phá hủy tất cả các viên gạch để chuyển màn

### Vật phẩm

| Biểu tượng | Tên           | Hiệu ứng                                      |
| ---------- | ------------- | --------------------------------------------- |


### Cách tính điểm

- Normal Brick: 100 points
- Strong Brick: 300 points
- Explosive Brick: 500 points + nearby bricks
- Power-up Collection: 50 points
- Combo Multiplier: x2, x3, x4... for consecutive hits

---

## Demo

### Screenshots


**Main Menu**  

**Gameplay**  

**Power-ups in Action**  

**Leaderboard**  

### Video Demo


_Full gameplay video is available in `docs/demo/gameplay.mp4`_

---

## Future Improvements


### Planned Features

1. **Additional game modes**
    
    - Time attack mode
    - Survival mode with endless levels
    - Co-op multiplayer mode
2. **Enhanced gameplay**
    
    - Boss battles at end of worlds
    - More power-up varieties (freeze time, shield wall, etc.)
    - Achievements system
3. **Technical improvements**
    
    - Migrate to LibGDX or JavaFX for better graphics
    - Add particle effects and advanced animations
    - Implement AI opponent mode
    - Add online leaderboard with database backend

---

## Technologies Used

| Công nghệ | Phiên bản | Vai trò                        |
| --------- | --------- | ------------------------------ |
| Java      | 24+       | Ngôn ngữ phát triển            |
| JavaFX    | 19.0.2    | Framework giao diện người dùng |
| Maven     | 3.9+      | Quản lý dự án và các phụ thuộc |


---

## Giấy phép

Dự án sử dụng cho mục đích giáo dục

**Liêm chính học thuật:** Dự án được cung cấp như một tài liệu tham khảo. Vui lòng tuân theo các tiêu chí liêm chính học thuật của cơ sở giáo dục

---

## Ghi chú

- Trò chơi được phát triển là một phần của môn học Lập trình hướng đối tượng với Java
- Các đoạn mã được viết bởi các thành viên dưới sự hướng dẫn
- Các nội dung hình ảnh, âm thanh được sử dụng với mục đích học thuật
- Bài tập lớn minh họa thực tế cách ứng dụng các nguyên tắc hướng đối tượng và mẫu thiết kế
