INSERT INTO item_categories (category_name) VALUES ('test');

INSERT INTO account_roles (role_name) VALUES ('seller');
INSERT INTO account_roles (role_name) VALUES ('customer');

INSERT INTO words (words_positive, words_negative) VALUES (null, null);
INSERT INTO words (words_positive, words_negative) VALUES (null, null);
INSERT INTO words (words_positive, words_negative) VALUES (null, null);
INSERT INTO words (words_positive, words_negative) VALUES (null, null);

INSERT INTO accounts (account_email, account_name, account_password, account_userName, account_role) VALUES ('initSeller email', 'initSeller name', '$2a$10$nUVvgTwR.l.a9E4GWEChXuj/mt23HWUDqeUdzW/Y6HMhwBuKYk21S', 'initSeller', 1);
INSERT INTO accounts (account_email, account_name, account_password, account_userName, account_role) VALUES ('initCus email', 'initCus name', 'initCus pass', 'initCus username', 2);

INSERT INTO items (item_title, item_account, item_category, item_rating, item_words) VALUES ('initItem title', 1, 1, 4, 1);
INSERT INTO items (item_title, item_account, item_category, item_rating, item_words) VALUES ('initItem title 2', 2, 1, 4, 2);

INSERT INTO reviews (review_title, review_body, review_date, review_rating, review_likes, review_dislikes, review_account, review_item) VALUES ('title', 'body', '2022-02-02', 4, 2, 2, 1, 1);

INSERT INTO reviews (review_title, review_body, review_date, review_rating, review_likes, review_dislikes, review_account, review_item) VALUES ('title', 'body', '2020-02-02', 4, 2, 2, 2, 2);
INSERT INTO reviews (review_title, review_body, review_date, review_rating, review_likes, review_dislikes, review_account, review_item) VALUES ('title', 'body', '2020-02-02', 4, 2, 2, 2, 2);
INSERT INTO reviews (review_title, review_body, review_date, review_rating, review_likes, review_dislikes, review_account, review_item) VALUES ('title', 'body', '2020-02-02', 4, 2, 2, 2, 2);