INSERT INTO mip.authority (description,name) VALUES 
('Acesso Estadual','ADMIN')
,('Gerenciador de Usuários','SYSTEM')
,('Responsável Técnico','USER')
;INSERT INTO mip.city (created_at,last_modified,name,state,created_by_id,modified_by_id) VALUES 
(NULL,NULL,'ASSIS','SP',NULL,NULL)
,(NULL,NULL,'Londrina','PR',NULL,NULL)
;INSERT INTO mip.farmer (created_at,last_modified,name,created_by_id,modified_by_id) VALUES 
(1575910086591,1575910086591,'João',1,1)
;INSERT INTO mip.field (created_at,last_modified,location,name,created_by_id,modified_by_id,city_id,farmer_id) VALUES 
(1576010006074,1576010006074,'1','Teste',1,1,1,1)
,(1576011180847,1576011180847,'1','Teste 45',1,1,2,1)
,(1576171153221,1576171153221,'32','Ateasdafdsad',1,1,1,1)
;INSERT INTO mip.field_supervisors (field_id,supervisors_id) VALUES 
(13,1)
,(14,1)
,(16,1)
;INSERT INTO mip.macro_region (created_at,last_modified,name,created_by_id,modified_by_id) VALUES 
(1575909981931,1575909981931,'Norte',1,1)
;INSERT INTO mip.mipuser (account_non_expired,account_non_locked,credentials_non_expired,email,enabled,full_name,password,username,city_id,region_id) VALUES 
(1,1,1,'teste@emater.pr.gov.br',1,'Teste','$2a$10$8nD8kx8nySvSDmMwwjPsG.KVoU3Uht1HkvIh6wz5QiZN8G0wgxth6','teste',NULL,NULL)
;INSERT INTO mip.mipuser_authorities (users_id,authorities_id) VALUES 
(1,1)
,(1,2)
,(1,3)
;INSERT INTO mip.region (created_at,last_modified,name,created_by_id,modified_by_id,macro_region_id) VALUES 
(1575910453030,1575910453030,'Região Teste',1,1,1)
;INSERT INTO mip.region_cities (region_id,cities_id) VALUES 
(1,1)
,(1,2)
;INSERT INTO mip.supervisor (created_at,last_modified,name,email,created_by_id,modified_by_id,region_id) VALUES 
(1575910494441,1575910494441,'Joao','jv.goulart.almeida@hotmail.com',1,1,1)
;