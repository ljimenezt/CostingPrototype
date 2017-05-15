--Eliminar los menus que tienen relaciones pero no estan siendo usados en la interfaz de usuario
--Eliminar la relacion rol_menu de estos menus
delete from seguridad.rol_menu rm
  where rm.id_menu=7
  or rm.id_menu=19
  or rm.id_menu=20
  or rm.id_menu=21
  or rm.id_menu=38
  or rm.id_menu=39
  or rm.id_menu=43
  or rm.id_menu=40
  or rm.id_menu=57
  or rm.id_menu=59
  or rm.id_menu=60
  or rm.id_menu=61
  or rm.id_menu=63
  or rm.id_menu=64
  or rm.id_menu=68
  or rm.id_menu=69
  or rm.id_menu=70
  or rm.id_menu=75
  or rm.id_menu=76
  or rm.id_menu=77
  or rm.id_menu=78
  or rm.id_menu=86
  or rm.id_menu=87
  or rm.id_menu=88
  or rm.id_menu=89
  or rm.id_menu=93
  or rm.id_menu=94
  or rm.id_menu=95
  or rm.id_menu=98
  or rm.id_menu=99
  or rm.id_menu=100
  or rm.id_menu=102
  or rm.id_menu=103
  or rm.id_menu=104
  or rm.id_menu=105
  or rm.id_menu=106
  or rm.id_menu=107
  or rm.id_menu=204
  or rm.id_menu=227
  or rm.id_menu=228
  or rm.id_menu=229
  or rm.id_menu=241
  or rm.id_menu=242
  or rm.id_menu=243
  or rm.id_menu=244
  or rm.id_menu=257
  or rm.id_menu=258
  or rm.id_menu=259
  or rm.id_menu=260
  or rm.id_menu=300
  or rm.id_menu=301
  or rm.id_menu=302
  or rm.id_menu=303
  or rm.id_menu=304
  or rm.id_menu=305
  or rm.id_menu=306
  or rm.id_menu=307
  or rm.id_menu=308
  or rm.id_menu=309
  or rm.id_menu=310
  or rm.id_menu=311
  or rm.id_menu=312
  or rm.id_menu=313
  or rm.id_menu=314
  or rm.id_menu=315
  or rm.id_menu=316
  or rm.id_menu=317
  or rm.id_menu=318
  or rm.id_menu=319
  or rm.id_menu=320
  or rm.id_menu=321
  or rm.id_menu=322
  or rm.id_menu=415
  or rm.id_menu=416
  or rm.id_menu=417
  or rm.id_menu=426
  or rm.id_menu=427
  or rm.id_menu=431
  or rm.id_menu=432
  or rm.id_menu=433
  or rm.id_menu=434
  or rm.id_menu=435
  or rm.id_menu=436
  or rm.id_menu=455
  or rm.id_menu=456;
  
--Eliminar la relacion metodo_menu de los menus mencionados anteriormente
delete from seguridad.metodo_menu rm
  where rm.id_menu=7
  or rm.id_menu=19
  or rm.id_menu=20
  or rm.id_menu=21
  or rm.id_menu=38
  or rm.id_menu=39
  or rm.id_menu=40
  or rm.id_menu=43
  or rm.id_menu=57
  or rm.id_menu=59
  or rm.id_menu=60
  or rm.id_menu=61
  or rm.id_menu=63
  or rm.id_menu=64
  or rm.id_menu=68
  or rm.id_menu=69
  or rm.id_menu=70
  or rm.id_menu=75
  or rm.id_menu=76
  or rm.id_menu=77
  or rm.id_menu=78
  or rm.id_menu=86
  or rm.id_menu=87
  or rm.id_menu=88
  or rm.id_menu=89
  or rm.id_menu=93
  or rm.id_menu=94
  or rm.id_menu=95
  or rm.id_menu=98
  or rm.id_menu=99
  or rm.id_menu=100
  or rm.id_menu=102
  or rm.id_menu=103
  or rm.id_menu=104
  or rm.id_menu=105
  or rm.id_menu=106
  or rm.id_menu=107
  or rm.id_menu=204
  or rm.id_menu=227
  or rm.id_menu=228
  or rm.id_menu=229
  or rm.id_menu=241
  or rm.id_menu=242
  or rm.id_menu=243
  or rm.id_menu=244
  or rm.id_menu=257
  or rm.id_menu=258
  or rm.id_menu=259
  or rm.id_menu=260
  or rm.id_menu=300
  or rm.id_menu=301
  or rm.id_menu=302
  or rm.id_menu=303
  or rm.id_menu=304
  or rm.id_menu=305
  or rm.id_menu=306
  or rm.id_menu=307
  or rm.id_menu=308
  or rm.id_menu=309
  or rm.id_menu=310
  or rm.id_menu=311
  or rm.id_menu=312
  or rm.id_menu=313
  or rm.id_menu=314
  or rm.id_menu=315
  or rm.id_menu=316
  or rm.id_menu=317
  or rm.id_menu=318
  or rm.id_menu=319
  or rm.id_menu=320
  or rm.id_menu=321
  or rm.id_menu=322
  or rm.id_menu=415
  or rm.id_menu=416
  or rm.id_menu=417
  or rm.id_menu=426
  or rm.id_menu=427
  or rm.id_menu=431
  or rm.id_menu=432
  or rm.id_menu=433
  or rm.id_menu=434
  or rm.id_menu=435
  or rm.id_menu=436
  or rm.id_menu=455
  or rm.id_menu=456;
  
 --Borrar los registros de los menus que no tienen relacion con la tabla rol_menu
delete from seguridad.menu m
    where m.id not in (select rm.id_menu from seguridad.rol_menu rm);	

--Cambiar el metodo que inicializa el registrar, el campo url de la tabla menu para el id=380
update seguridad.menu set url='typeOfManagementAction.addEditTypeOfManagement(null)'
  where id=380;	
  
-- Eliminar los iconos que no estan relacionados a la tabla menu
delete from seguridad.icono i
    where i.id not in (select m.id_icono from seguridad.menu m 
	                     where m.id_icono is not null);
						 

--Eliminar los metodos que no se estan usando
--Eliminar primero las relaciones con rol_metodo
delete from seguridad.rol_metodo rm
  where rm.id_metodo=15
  or rm.id_metodo=16
  or rm.id_metodo=17 
  or rm.id_metodo=20
  or rm.id_metodo=21
  or rm.id_metodo=22
  or rm.id_metodo=25
  or rm.id_metodo=26
  or rm.id_metodo=27
  or rm.id_metodo=28
  or rm.id_metodo=29
  or rm.id_metodo=30
  or rm.id_metodo=31
  or rm.id_metodo=32
  or rm.id_metodo=33
  or rm.id_metodo=34
  or rm.id_metodo=35
  or rm.id_metodo=36
  or rm.id_metodo=37
  or rm.id_metodo=38
  or rm.id_metodo=39
  or rm.id_metodo=40
  or rm.id_metodo=56
  or rm.id_metodo=62
  or rm.id_metodo=70
  or rm.id_metodo=73
  or rm.id_metodo=75
  or rm.id_metodo=76
  or rm.id_metodo=77
  or rm.id_metodo=87
  or rm.id_metodo=93;  
 
 
--Eliminar los metodos que no estan relacionados con rol_menu
delete from seguridad.metodo m
    where m.id not in (select mm.id_metodo from seguridad.metodo_menu mm);
	


	