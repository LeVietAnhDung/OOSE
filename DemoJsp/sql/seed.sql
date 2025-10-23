USE team3;


-- Ngôn ngữ
INSERT INTO Language(LanguageID, Language) VALUES
  ('vi','Tiếng Việt'),
  ('en','English'),
  ('ja','日本語'),
  ('fr','Français');

-- Danh mục (4 danh mục)
INSERT INTO ProductCategory (CanBeShipped) VALUES (b'1'),(b'1'),(b'1'),(b'0');  -- IDs 1..4

-- Tên danh mục theo 3 ngôn ngữ
INSERT INTO ProductCategoryTranslation(ProductCategoryID, LanguageID, CategoryName) VALUES
-- ID 1
(1,'vi','Điện thoại'), (1,'en','Phones'), (1,'ja','電話'), (1,'fr','Téléphones'),
-- ID 2
(2,'vi','Phụ kiện'),  (2,'en','Accessories'), (2,'ja','アクセサリー'), (2,'fr','Accessoires'),
-- ID 3
(3,'vi','Laptop'),    (3,'en','Laptops'), (3,'ja','ノートPC'), (3,'fr','PC portables'),
-- ID 4
(4,'vi','Phần mềm'),  (4,'en','Software'), (4,'ja','ソフトウェア'), (4,'fr','Logiciels');

-- SẢN PHẨM: mỗi danh mục 3 sp (tổng 12 sp)
INSERT INTO Product (Price,Weight,ProductCategoryID) VALUES
-- Cat 1: Điện thoại (1)
(12000000, 180.0, 1),  -- ID 1
(15000000, 200.0, 1),  -- ID 2
(10000000, 170.0, 1),  -- ID 3
-- Cat 2: Phụ kiện (2)
(250000,    20.0, 2),  -- ID 4
(500000,    15.0, 2),  -- ID 5
(350000,    25.0, 2),  -- ID 6
-- Cat 3: Laptop (3)
(18000000, 2200.0, 3), -- ID 7
(22000000, 2000.0, 3), -- ID 8
(20000000, 2100.0, 3), -- ID 9
-- Cat 4: Phần mềm (4)
(2000000,     0.0, 4), -- ID 10
(1500000,     0.0, 4), -- ID 11
(3000000,     0.0, 4); -- ID 12

-- DỊCH SẢN PHẨM (VI/EN/JA)
INSERT INTO ProductTranslation(ProductID, LanguageID, ProductName, ProductDescription) VALUES
-- Phones
(1,'vi','Điện thoại A1','Màn hình 6.1"'),
(1,'en','Phone A1','6.1" display'),
(1,'fr','Phone A1','6.1" display'),
(1,'ja','A1 スマートフォン','6.1インチ ディスプレイ'),

(2,'vi','Điện thoại X2','Pin 5000mAh, camera kép'),
(2,'en','Phone X2','5000mAh battery, dual camera'),
(2,'fr','Phone X2','5000mAh battery, dual camera'),
(2,'ja','X2 スマートフォン','5000mAh バッテリー、デュアルカメラ'),

(3,'vi','Điện thoại Y3','Chip 8 nhân, RAM 8GB'),
(3,'en','Phone Y3','Octa-core chip, 8GB RAM'),
(3,'fr','Phone Y3','Octa-core chip, 8GB RAM'),
(3,'ja','Y3 スマートフォン','オクタコア、8GB RAM'),

-- Accessories
(4,'vi','Tai nghe B2','Bluetooth 5.0'),
(4,'en','Headset B2','Bluetooth 5.0'),
(4,'fr','Headset B2','Bluetooth 5.0'),
(4,'ja','B2 ヘッドセット','Bluetooth 5.0'),

(5,'vi','Sạc nhanh C3','Hỗ trợ sạc 65W'),
(5,'en','Fast Charger C3','Supports 65W charging'),
(5,'fr','Fast Charger C3','Supports 65W charging'),
(5,'ja','C3 急速充電器','65W 充電対応'),

(6,'vi','Chuột D4','Chuột không dây 2.4GHz'),
(6,'en','Mouse D4','Wireless mouse 2.4GHz'),
(6,'fr','Mouse D4','Wireless mouse 2.4GHz'),
(6,'ja','D4 ワイヤレスマウス','2.4GHz ワイヤレス'),

-- Laptops
(7,'vi','Laptop L5','Core i5, 16GB RAM, SSD 512GB'),
(7,'en','Laptop L5','Core i5, 16GB RAM, 512GB SSD'),
(7,'fr','Laptop L5','Core i5, 16GB RAM, 512GB SSD'),
(7,'ja','ノートPC L5','Core i5、16GB RAM、512GB SSD'),

(8,'vi','Laptop Pro 14','Core i7, màn hình 14" Retina'),
(8,'en','Laptop Pro 14','Core i7, 14" Retina display'),
(8,'fr','Laptop Pro 14','Core i7, 14" Retina display'),
(8,'ja','ノートPC Pro 14','Core i7、14インチ Retina'),

(9,'vi','Laptop Gamer Z','RTX 4060, màn 15.6" 144Hz'),
(9,'en','Gaming Laptop Z','RTX 4060, 15.6" 144Hz'),
(9,'fr','Gaming Laptop Z','RTX 4060, 15.6" 144Hz'),
(9,'ja','ゲーミングノート Z','RTX 4060、15.6インチ 144Hz'),

-- Software
(10,'vi','Phần mềm kế toán EBiz','Quản lý tài chính cho SME'),
(10,'en','Accounting Software EBiz','Finance management for SMEs'),
(10,'fr','Accounting Software EBiz','Finance management for SMEs'),
(10,'ja','会計ソフト EBiz','中小企業向け財務管理'),

(11,'vi','Phần mềm CRM Pro','Quản lý khách hàng'),
(11,'en','CRM Software Pro','Customer management'),
(11,'fr','CRM Software Pro','Customer management'),
(11,'ja','CRM ソフト Pro','顧客管理'),

(12,'vi','Phần mềm HRM','Quản lý nhân sự'),
(12,'en','HRM Software','Human Resource Management'),
(12,'fr','HRM Software','Human Resource Management'),
(12,'ja','HRM ソフト','人事管理');