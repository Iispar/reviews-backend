INSERT INTO item_category (category_id, category_name) VALUES (1, "test");

INSERT INTO role (role_id, role_name) VALUES (1, "seller");
INSERT INTO role (role_id, role_name) VALUES (2, "customer");

INSERT INTO words (words_id, words_positive, words_negative) VALUES (1, null, null);

INSERT INTO user (user_id, user_email, user_name, user_password, user_username, user_role) VALUES (1, "initSeller email", "initSeller name", "initSeller pass", "initSeller username", 1); 
INSERT INTO user (user_id, user_email, user_name, user_password, user_username, user_role) VALUES (2, "initCus email", "initCus name", "initCus pass", "initCus username", 2);

INSERT INTO item (item_id, item_title, item_user, item_category, item_rating, item_words) VALUES (1, "initItem title", 1, 1, "4", 1);