USE FitnessManagement;

-- 创建用户表
CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(50) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role ENUM('user', 'coach') DEFAULT 'user',
                       target ENUM('减脂', '增肌', '维持') DEFAULT '减脂',
                       weight FLOAT,
                       height FLOAT,
                       bmi FLOAT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建健身计划表
CREATE TABLE Plans (
                       user_id INT NOT NULL,
                       plan_id INT NOT NULL,
                       exercise_name VARCHAR(50) NOT NULL,
                       duration INT NOT NULL, -- 单位：分钟
                       calories_burned FLOAT NOT NULL,
                       day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (user_id, plan_id), -- 复合主键
                       FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 创建运动数据表
CREATE TABLE ActivityData (
                              data_id INT PRIMARY KEY AUTO_INCREMENT,
                              user_id INT NOT NULL,
                              date DATE NOT NULL,
                              steps INT NOT NULL,
                              calories FLOAT NOT NULL,
                              heart_rate INT NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 创建社交动态表
CREATE TABLE SocialPosts (
                             post_id INT PRIMARY KEY AUTO_INCREMENT,
                             user_id INT NOT NULL,
                             content TEXT NOT NULL,
                             image_url VARCHAR(255),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 创建点赞表
CREATE TABLE PostLikes (
                           like_id INT PRIMARY KEY AUTO_INCREMENT,
                           post_id INT NOT NULL,
                           user_id INT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (post_id) REFERENCES SocialPosts(post_id) ON DELETE CASCADE,
                           FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 创建评论表
CREATE TABLE PostComments (
                              comment_id INT PRIMARY KEY AUTO_INCREMENT,
                              post_id INT NOT NULL,
                              user_id INT NOT NULL,
                              content TEXT NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (post_id) REFERENCES SocialPosts(post_id) ON DELETE CASCADE,
                              FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 创建教练反馈表
CREATE TABLE CoachFeedback (
                               feedback_id INT PRIMARY KEY AUTO_INCREMENT,
                               coach_id INT NOT NULL,
                               user_id INT NOT NULL,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (coach_id) REFERENCES Users(user_id) ON DELETE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

