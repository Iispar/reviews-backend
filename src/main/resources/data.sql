INSERT INTO item_categories (category_name) VALUES ('test');

INSERT INTO account_roles (role_name) VALUES ('seller');
INSERT INTO account_roles (role_name) VALUES ('customer');

INSERT INTO words (words_positive, words_negative) VALUES (null, null);
INSERT INTO words (words_positive, words_negative) VALUES (null, null);
INSERT INTO words (words_positive, words_negative) VALUES (null, null);

INSERT INTO accounts (account_email, account_name, account_password, account_userName, account_role) VALUES ('initSeller email', 'initSeller name', 'initSeller pass', 'initSeller username', 1);
INSERT INTO accounts (account_email, account_name, account_password, account_userName, account_role) VALUES ('initCus email', 'initCus name', 'initCus pass', 'initCus username', 2);

INSERT INTO items (item_title, item_account, item_category, item_rating, item_words, item_desc) VALUES ('initItem title', 1, 1, 4, 1, 'test desc');