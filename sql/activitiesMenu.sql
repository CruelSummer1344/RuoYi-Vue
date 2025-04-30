use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动', '0', '1', 'activities', 'activities/activities/index', 1, 0, 'C', '0', '0', 'activities:activities:list', '#', 'admin', sysdate(), '', null, '活动菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'activities:activities:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'activities:activities:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'activities:activities:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'activities:activities:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('活动导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'activities:activities:export',       '#', 'admin', sysdate(), '', null, '');