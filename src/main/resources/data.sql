INSERT INTO item_categories (category_id, category_name) VALUES (1, 'test');

INSERT INTO account_roles (role_id, role_name) VALUES (1, 'seller');
INSERT INTO account_roles (role_id, role_name) VALUES (2, 'customer');

INSERT INTO words (words_id, words_positive, words_negative) VALUES (1, null, null);
INSERT INTO words (words_id, words_positive, words_negative) VALUES (2, null, null);

INSERT INTO accounts (account_id, account_email, account_name, account_password, account_username, account_role) VALUES (1, 'initSeller email', 'initSeller name', 'initSeller pass', 'initSeller accountname', 1); 
INSERT INTO accounts (account_id, account_email, account_name, account_password, account_username, account_role) VALUES (2, 'initCus email', 'initCus name', 'initCus pass', 'initCus accountname', 2);

INSERT INTO items (item_id, item_title, item_account, item_category, item_rating, item_words, item_desc) VALUES (1, 'initItem title', 1, 1, 4, 1, 'test desc');