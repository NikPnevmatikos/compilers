@.BT_vtable = global [1 x i8*] [
	i8* bitcast (i32 (i8*)* @BT.Start to i8*)
]

@.Tree_vtable = global [20 x i8*] [
	i8* bitcast (i1 (i8*, i32)* @Tree.Init to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.SetRight to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.SetLeft to i8*),
	i8* bitcast (i8* (i8*)* @Tree.GetRight to i8*),
	i8* bitcast (i8* (i8*)* @Tree.GetLeft to i8*),
	i8* bitcast (i32 (i8*)* @Tree.GetKey to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.SetKey to i8*),
	i8* bitcast (i1 (i8*)* @Tree.GetHas_Right to i8*),
	i8* bitcast (i1 (i8*)* @Tree.GetHas_Left to i8*),
	i8* bitcast (i1 (i8*, i1)* @Tree.SetHas_Left to i8*),
	i8* bitcast (i1 (i8*, i1)* @Tree.SetHas_Right to i8*),
	i8* bitcast (i1 (i8*, i32, i32)* @Tree.Compare to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.Insert to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.Delete to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.Remove to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.RemoveRight to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.RemoveLeft to i8*),
	i8* bitcast (i32 (i8*, i32)* @Tree.Search to i8*),
	i8* bitcast (i1 (i8*)* @Tree.Print to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.RecPrint to i8*)
]

declare i8* @calloc(i32, i32)
declare i32 @printf(i8*, ...)
declare void @exit(i32)

@_cint = constant [4 x i8] c"%d\0a\00" 
@_cOOB = constant [15 x i8] c"Out of bounds\0a\00"

define void @print_int(i32 %i) {
	%_str = bitcast [4 x i8]* @_cint to i8*
	call i32 (i8*, ...) @printf(i8* %_str, i32 %i)
	ret void
}

define void @throw_oob() {
	%_str = bitcast [15 x i8]* @_cOOB to i8*
	call i32 (i8*, ...) @printf(i8* %_str)
	call void @exit(i32 1)
	ret void
}

define i32 @main() {


	; Allocating object type BT
	%_0 = call i8* @calloc(i32 1, i32 8)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.BT_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	; BT.Start(): 0
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*)*
	%_8 = call i32 %_7(i8* %_0)

	call void (i32) @print_int(i32 %_8)

	ret i32 0
}

define i32 @BT.Start(i8* %this) {

	%root = alloca i8*
	%ntb = alloca i1
	%nti = alloca i32

	; Allocating object type Tree
	%_0 = call i8* @calloc(i32 1, i32 38)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [20 x i8*], [20 x i8*]* @.Tree_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %root

	%_3 = load i8*, i8** %root
	; Tree.Init(): 0
	%_4 = bitcast i8* %_3 to i8***
	%_5 = load i8**, i8*** %_4
	%_6 = getelementptr i8*, i8** %_5, i32 0
	%_7 = load i8*, i8** %_6
	%_8 = bitcast i8* %_7 to i1 (i8*, i32)*
	%_9 = call i1 %_8(i8* %_3,i32 16)
	store i1 %_9, i1* %ntb

	%_10 = load i8*, i8** %root
	; Tree.Print(): 18
	%_11 = bitcast i8* %_10 to i8***
	%_12 = load i8**, i8*** %_11
	%_13 = getelementptr i8*, i8** %_12, i32 18
	%_14 = load i8*, i8** %_13
	%_15 = bitcast i8* %_14 to i1 (i8*)*
	%_16 = call i1 %_15(i8* %_10)
	store i1 %_16, i1* %ntb


	call void (i32) @print_int(i32 100000000)
	%_17 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_18 = bitcast i8* %_17 to i8***
	%_19 = load i8**, i8*** %_18
	%_20 = getelementptr i8*, i8** %_19, i32 12
	%_21 = load i8*, i8** %_20
	%_22 = bitcast i8* %_21 to i1 (i8*, i32)*
	%_23 = call i1 %_22(i8* %_17,i32 8)
	store i1 %_23, i1* %ntb

	%_24 = load i8*, i8** %root
	; Tree.Print(): 18
	%_25 = bitcast i8* %_24 to i8***
	%_26 = load i8**, i8*** %_25
	%_27 = getelementptr i8*, i8** %_26, i32 18
	%_28 = load i8*, i8** %_27
	%_29 = bitcast i8* %_28 to i1 (i8*)*
	%_30 = call i1 %_29(i8* %_24)
	store i1 %_30, i1* %ntb

	%_31 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_32 = bitcast i8* %_31 to i8***
	%_33 = load i8**, i8*** %_32
	%_34 = getelementptr i8*, i8** %_33, i32 12
	%_35 = load i8*, i8** %_34
	%_36 = bitcast i8* %_35 to i1 (i8*, i32)*
	%_37 = call i1 %_36(i8* %_31,i32 24)
	store i1 %_37, i1* %ntb

	%_38 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_39 = bitcast i8* %_38 to i8***
	%_40 = load i8**, i8*** %_39
	%_41 = getelementptr i8*, i8** %_40, i32 12
	%_42 = load i8*, i8** %_41
	%_43 = bitcast i8* %_42 to i1 (i8*, i32)*
	%_44 = call i1 %_43(i8* %_38,i32 4)
	store i1 %_44, i1* %ntb

	%_45 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_46 = bitcast i8* %_45 to i8***
	%_47 = load i8**, i8*** %_46
	%_48 = getelementptr i8*, i8** %_47, i32 12
	%_49 = load i8*, i8** %_48
	%_50 = bitcast i8* %_49 to i1 (i8*, i32)*
	%_51 = call i1 %_50(i8* %_45,i32 12)
	store i1 %_51, i1* %ntb

	%_52 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_53 = bitcast i8* %_52 to i8***
	%_54 = load i8**, i8*** %_53
	%_55 = getelementptr i8*, i8** %_54, i32 12
	%_56 = load i8*, i8** %_55
	%_57 = bitcast i8* %_56 to i1 (i8*, i32)*
	%_58 = call i1 %_57(i8* %_52,i32 20)
	store i1 %_58, i1* %ntb

	%_59 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_60 = bitcast i8* %_59 to i8***
	%_61 = load i8**, i8*** %_60
	%_62 = getelementptr i8*, i8** %_61, i32 12
	%_63 = load i8*, i8** %_62
	%_64 = bitcast i8* %_63 to i1 (i8*, i32)*
	%_65 = call i1 %_64(i8* %_59,i32 28)
	store i1 %_65, i1* %ntb

	%_66 = load i8*, i8** %root
	; Tree.Insert(): 12
	%_67 = bitcast i8* %_66 to i8***
	%_68 = load i8**, i8*** %_67
	%_69 = getelementptr i8*, i8** %_68, i32 12
	%_70 = load i8*, i8** %_69
	%_71 = bitcast i8* %_70 to i1 (i8*, i32)*
	%_72 = call i1 %_71(i8* %_66,i32 14)
	store i1 %_72, i1* %ntb

	%_73 = load i8*, i8** %root
	; Tree.Print(): 18
	%_74 = bitcast i8* %_73 to i8***
	%_75 = load i8**, i8*** %_74
	%_76 = getelementptr i8*, i8** %_75, i32 18
	%_77 = load i8*, i8** %_76
	%_78 = bitcast i8* %_77 to i1 (i8*)*
	%_79 = call i1 %_78(i8* %_73)
	store i1 %_79, i1* %ntb

	%_80 = load i8*, i8** %root
	; Tree.Search(): 17
	%_81 = bitcast i8* %_80 to i8***
	%_82 = load i8**, i8*** %_81
	%_83 = getelementptr i8*, i8** %_82, i32 17
	%_84 = load i8*, i8** %_83
	%_85 = bitcast i8* %_84 to i32 (i8*, i32)*
	%_86 = call i32 %_85(i8* %_80,i32 24)

	call void (i32) @print_int(i32 %_86)
	%_87 = load i8*, i8** %root
	; Tree.Search(): 17
	%_88 = bitcast i8* %_87 to i8***
	%_89 = load i8**, i8*** %_88
	%_90 = getelementptr i8*, i8** %_89, i32 17
	%_91 = load i8*, i8** %_90
	%_92 = bitcast i8* %_91 to i32 (i8*, i32)*
	%_93 = call i32 %_92(i8* %_87,i32 12)

	call void (i32) @print_int(i32 %_93)
	%_94 = load i8*, i8** %root
	; Tree.Search(): 17
	%_95 = bitcast i8* %_94 to i8***
	%_96 = load i8**, i8*** %_95
	%_97 = getelementptr i8*, i8** %_96, i32 17
	%_98 = load i8*, i8** %_97
	%_99 = bitcast i8* %_98 to i32 (i8*, i32)*
	%_100 = call i32 %_99(i8* %_94,i32 16)

	call void (i32) @print_int(i32 %_100)
	%_101 = load i8*, i8** %root
	; Tree.Search(): 17
	%_102 = bitcast i8* %_101 to i8***
	%_103 = load i8**, i8*** %_102
	%_104 = getelementptr i8*, i8** %_103, i32 17
	%_105 = load i8*, i8** %_104
	%_106 = bitcast i8* %_105 to i32 (i8*, i32)*
	%_107 = call i32 %_106(i8* %_101,i32 50)

	call void (i32) @print_int(i32 %_107)
	%_108 = load i8*, i8** %root
	; Tree.Search(): 17
	%_109 = bitcast i8* %_108 to i8***
	%_110 = load i8**, i8*** %_109
	%_111 = getelementptr i8*, i8** %_110, i32 17
	%_112 = load i8*, i8** %_111
	%_113 = bitcast i8* %_112 to i32 (i8*, i32)*
	%_114 = call i32 %_113(i8* %_108,i32 12)

	call void (i32) @print_int(i32 %_114)
	%_115 = load i8*, i8** %root
	; Tree.Delete(): 13
	%_116 = bitcast i8* %_115 to i8***
	%_117 = load i8**, i8*** %_116
	%_118 = getelementptr i8*, i8** %_117, i32 13
	%_119 = load i8*, i8** %_118
	%_120 = bitcast i8* %_119 to i1 (i8*, i32)*
	%_121 = call i1 %_120(i8* %_115,i32 12)
	store i1 %_121, i1* %ntb

	%_122 = load i8*, i8** %root
	; Tree.Print(): 18
	%_123 = bitcast i8* %_122 to i8***
	%_124 = load i8**, i8*** %_123
	%_125 = getelementptr i8*, i8** %_124, i32 18
	%_126 = load i8*, i8** %_125
	%_127 = bitcast i8* %_126 to i1 (i8*)*
	%_128 = call i1 %_127(i8* %_122)
	store i1 %_128, i1* %ntb

	%_129 = load i8*, i8** %root
	; Tree.Search(): 17
	%_130 = bitcast i8* %_129 to i8***
	%_131 = load i8**, i8*** %_130
	%_132 = getelementptr i8*, i8** %_131, i32 17
	%_133 = load i8*, i8** %_132
	%_134 = bitcast i8* %_133 to i32 (i8*, i32)*
	%_135 = call i32 %_134(i8* %_129,i32 12)

	call void (i32) @print_int(i32 %_135)

	ret i32 0

}

define i1 @Tree.Init(i8* %this, i32 %.v_key) {

	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key


	%_0 = getelementptr i8, i8* %this, i32 24
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %v_key
	store i32 %_2, i32* %_1

	%_3 = getelementptr i8, i8* %this, i32 28
	%_4 = bitcast i8* %_3 to i1*
	store i1 0, i1* %_4

	%_5 = getelementptr i8, i8* %this, i32 29
	%_6 = bitcast i8* %_5 to i1*
	store i1 0, i1* %_6


	ret i1 1

}

define i1 @Tree.SetRight(i8* %this, i8* %.rn) {

	%rn = alloca i8*
	store i8* %.rn, i8** %rn


	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i8**
	%_2 = load i8*, i8** %rn
	store i8* %_2, i8** %_1


	ret i1 1

}

define i1 @Tree.SetLeft(i8* %this, i8* %.ln) {

	%ln = alloca i8*
	store i8* %.ln, i8** %ln


	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i8**
	%_2 = load i8*, i8** %ln
	store i8* %_2, i8** %_1


	ret i1 1

}

define i8* @Tree.GetRight(i8* %this) {


	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i8**
	%_2 = load i8*, i8** %_1

	ret i8* %_2

}

define i8* @Tree.GetLeft(i8* %this) {


	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i8**
	%_2 = load i8*, i8** %_1

	ret i8* %_2

}

define i32 @Tree.GetKey(i8* %this) {


	%_0 = getelementptr i8, i8* %this, i32 24
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1

	ret i32 %_2

}

define i1 @Tree.SetKey(i8* %this, i32 %.v_key) {

	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key


	%_0 = getelementptr i8, i8* %this, i32 24
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %v_key
	store i32 %_2, i32* %_1


	ret i1 1

}

define i1 @Tree.GetHas_Right(i8* %this) {


	%_0 = getelementptr i8, i8* %this, i32 29
	%_1 = bitcast i8* %_0 to i1*
	%_2 = load i1, i1* %_1

	ret i1 %_2

}

define i1 @Tree.GetHas_Left(i8* %this) {


	%_0 = getelementptr i8, i8* %this, i32 28
	%_1 = bitcast i8* %_0 to i1*
	%_2 = load i1, i1* %_1

	ret i1 %_2

}

define i1 @Tree.SetHas_Left(i8* %this, i1 %.val) {

	%val = alloca i1
	store i1 %.val, i1* %val


	%_0 = getelementptr i8, i8* %this, i32 28
	%_1 = bitcast i8* %_0 to i1*
	%_2 = load i1, i1* %val
	store i1 %_2, i1* %_1


	ret i1 1

}

define i1 @Tree.SetHas_Right(i8* %this, i1 %.val) {

	%val = alloca i1
	store i1 %.val, i1* %val


	%_0 = getelementptr i8, i8* %this, i32 29
	%_1 = bitcast i8* %_0 to i1*
	%_2 = load i1, i1* %val
	store i1 %_2, i1* %_1


	ret i1 1

}

define i1 @Tree.Compare(i8* %this, i32 %.num1, i32 %.num2) {

	%num1 = alloca i32
	store i32 %.num1, i32* %num1

	%num2 = alloca i32
	store i32 %.num2, i32* %num2

	%ntb = alloca i1
	%nti = alloca i32

	store i1 0, i1* %ntb

	%_0 = load i32, i32* %num2
	%_1 = add i32 %_0, 1
	store i32 %_1, i32* %nti

	; If Statment
	%_2 = load i32, i32* %num1
	%_3 = load i32, i32* %num2
	%_4 = icmp slt i32 %_2, %_3
	br i1 %_4, label %iflabel0, label %ellabel1

iflabel0:
	store i1 0, i1* %ntb

	br label %exitif2

ellabel1:
	; If Statment
	%_5 = load i32, i32* %num1
	%_6 = load i32, i32* %nti
	%_7 = icmp slt i32 %_5, %_6
	%_8 = xor i1 1, %_7
	br i1 %_8, label %iflabel3, label %ellabel4

iflabel3:
	store i1 0, i1* %ntb

	br label %exitif5

ellabel4:
	store i1 1, i1* %ntb

	br label %exitif5

exitif5:

	br label %exitif2

exitif2:

	%_9 = load i1, i1* %ntb

	ret i1 %_9

}

define i1 @Tree.Insert(i8* %this, i32 %.v_key) {

	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key

	%new_node = alloca i8*
	%ntb = alloca i1
	%cont = alloca i1
	%key_aux = alloca i32
	%current_node = alloca i8*

	; Allocating object type Tree
	%_0 = call i8* @calloc(i32 1, i32 38)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [20 x i8*], [20 x i8*]* @.Tree_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %new_node

	%_3 = load i8*, i8** %new_node
	; Tree.Init(): 0
	%_4 = bitcast i8* %_3 to i8***
	%_5 = load i8**, i8*** %_4
	%_6 = getelementptr i8*, i8** %_5, i32 0
	%_7 = load i8*, i8** %_6
	%_8 = bitcast i8* %_7 to i1 (i8*, i32)*
	%_9 = load i32, i32* %v_key
	%_10 = call i1 %_8(i8* %_3,i32 %_9)
	store i1 %_10, i1* %ntb

	store i8* %this, i8** %current_node

	store i1 1, i1* %cont

	; While Statment
	br label %loop0
loop0:
	%_11 = load i1, i1* %cont
	br i1 %_11, label %statments1, label %exitwhile2
statments1:
	%_12 = load i8*, i8** %current_node
	; Tree.GetKey(): 5
	%_13 = bitcast i8* %_12 to i8***
	%_14 = load i8**, i8*** %_13
	%_15 = getelementptr i8*, i8** %_14, i32 5
	%_16 = load i8*, i8** %_15
	%_17 = bitcast i8* %_16 to i32 (i8*)*
	%_18 = call i32 %_17(i8* %_12)
	store i32 %_18, i32* %key_aux

	; If Statment
	%_19 = load i32, i32* %v_key
	%_20 = load i32, i32* %key_aux
	%_21 = icmp slt i32 %_19, %_20
	br i1 %_21, label %iflabel6, label %ellabel7

iflabel6:
	; If Statment
	%_22 = load i8*, i8** %current_node
	; Tree.GetHas_Left(): 8
	%_23 = bitcast i8* %_22 to i8***
	%_24 = load i8**, i8*** %_23
	%_25 = getelementptr i8*, i8** %_24, i32 8
	%_26 = load i8*, i8** %_25
	%_27 = bitcast i8* %_26 to i1 (i8*)*
	%_28 = call i1 %_27(i8* %_22)
	br i1 %_28, label %iflabel9, label %ellabel10

iflabel9:
	%_29 = load i8*, i8** %current_node
	; Tree.GetLeft(): 4
	%_30 = bitcast i8* %_29 to i8***
	%_31 = load i8**, i8*** %_30
	%_32 = getelementptr i8*, i8** %_31, i32 4
	%_33 = load i8*, i8** %_32
	%_34 = bitcast i8* %_33 to i8* (i8*)*
	%_35 = call i8* %_34(i8* %_29)
	store i8* %_35, i8** %current_node

	br label %exitif11

ellabel10:
	store i1 0, i1* %cont

	%_36 = load i8*, i8** %current_node
	; Tree.SetHas_Left(): 9
	%_37 = bitcast i8* %_36 to i8***
	%_38 = load i8**, i8*** %_37
	%_39 = getelementptr i8*, i8** %_38, i32 9
	%_40 = load i8*, i8** %_39
	%_41 = bitcast i8* %_40 to i1 (i8*, i1)*
	%_42 = call i1 %_41(i8* %_36,i1 1)
	store i1 %_42, i1* %ntb

	%_43 = load i8*, i8** %current_node
	; Tree.SetLeft(): 2
	%_44 = bitcast i8* %_43 to i8***
	%_45 = load i8**, i8*** %_44
	%_46 = getelementptr i8*, i8** %_45, i32 2
	%_47 = load i8*, i8** %_46
	%_48 = bitcast i8* %_47 to i1 (i8*, i8*)*
	%_49 = load i8*, i8** %new_node
	%_50 = call i1 %_48(i8* %_43,i8* %_49)
	store i1 %_50, i1* %ntb

	br label %exitif11

exitif11:

	br label %exitif8

ellabel7:
	; If Statment
	%_51 = load i8*, i8** %current_node
	; Tree.GetHas_Right(): 7
	%_52 = bitcast i8* %_51 to i8***
	%_53 = load i8**, i8*** %_52
	%_54 = getelementptr i8*, i8** %_53, i32 7
	%_55 = load i8*, i8** %_54
	%_56 = bitcast i8* %_55 to i1 (i8*)*
	%_57 = call i1 %_56(i8* %_51)
	br i1 %_57, label %iflabel12, label %ellabel13

iflabel12:
	%_58 = load i8*, i8** %current_node
	; Tree.GetRight(): 3
	%_59 = bitcast i8* %_58 to i8***
	%_60 = load i8**, i8*** %_59
	%_61 = getelementptr i8*, i8** %_60, i32 3
	%_62 = load i8*, i8** %_61
	%_63 = bitcast i8* %_62 to i8* (i8*)*
	%_64 = call i8* %_63(i8* %_58)
	store i8* %_64, i8** %current_node

	br label %exitif14

ellabel13:
	store i1 0, i1* %cont

	%_65 = load i8*, i8** %current_node
	; Tree.SetHas_Right(): 10
	%_66 = bitcast i8* %_65 to i8***
	%_67 = load i8**, i8*** %_66
	%_68 = getelementptr i8*, i8** %_67, i32 10
	%_69 = load i8*, i8** %_68
	%_70 = bitcast i8* %_69 to i1 (i8*, i1)*
	%_71 = call i1 %_70(i8* %_65,i1 1)
	store i1 %_71, i1* %ntb

	%_72 = load i8*, i8** %current_node
	; Tree.SetRight(): 1
	%_73 = bitcast i8* %_72 to i8***
	%_74 = load i8**, i8*** %_73
	%_75 = getelementptr i8*, i8** %_74, i32 1
	%_76 = load i8*, i8** %_75
	%_77 = bitcast i8* %_76 to i1 (i8*, i8*)*
	%_78 = load i8*, i8** %new_node
	%_79 = call i1 %_77(i8* %_72,i8* %_78)
	store i1 %_79, i1* %ntb

	br label %exitif14

exitif14:

	br label %exitif8

exitif8:

	br label %loop0
exitwhile2:

	ret i1 1

}

define i1 @Tree.Delete(i8* %this, i32 %.v_key) {

	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key

	%current_node = alloca i8*
	%parent_node = alloca i8*
	%cont = alloca i1
	%found = alloca i1
	%is_root = alloca i1
	%key_aux = alloca i32
	%ntb = alloca i1

	store i8* %this, i8** %current_node

	store i8* %this, i8** %parent_node

	store i1 1, i1* %cont

	store i1 0, i1* %found

	store i1 1, i1* %is_root

	; While Statment
	br label %loop3
loop3:
	%_0 = load i1, i1* %cont
	br i1 %_0, label %statments4, label %exitwhile5
statments4:
	%_1 = load i8*, i8** %current_node
	; Tree.GetKey(): 5
	%_2 = bitcast i8* %_1 to i8***
	%_3 = load i8**, i8*** %_2
	%_4 = getelementptr i8*, i8** %_3, i32 5
	%_5 = load i8*, i8** %_4
	%_6 = bitcast i8* %_5 to i32 (i8*)*
	%_7 = call i32 %_6(i8* %_1)
	store i32 %_7, i32* %key_aux

	; If Statment
	%_8 = load i32, i32* %v_key
	%_9 = load i32, i32* %key_aux
	%_10 = icmp slt i32 %_8, %_9
	br i1 %_10, label %iflabel15, label %ellabel16

iflabel15:
	; If Statment
	%_11 = load i8*, i8** %current_node
	; Tree.GetHas_Left(): 8
	%_12 = bitcast i8* %_11 to i8***
	%_13 = load i8**, i8*** %_12
	%_14 = getelementptr i8*, i8** %_13, i32 8
	%_15 = load i8*, i8** %_14
	%_16 = bitcast i8* %_15 to i1 (i8*)*
	%_17 = call i1 %_16(i8* %_11)
	br i1 %_17, label %iflabel18, label %ellabel19

iflabel18:
	%_18 = load i8*, i8** %current_node
	store i8* %_18, i8** %parent_node

	%_19 = load i8*, i8** %current_node
	; Tree.GetLeft(): 4
	%_20 = bitcast i8* %_19 to i8***
	%_21 = load i8**, i8*** %_20
	%_22 = getelementptr i8*, i8** %_21, i32 4
	%_23 = load i8*, i8** %_22
	%_24 = bitcast i8* %_23 to i8* (i8*)*
	%_25 = call i8* %_24(i8* %_19)
	store i8* %_25, i8** %current_node

	br label %exitif20

ellabel19:
	store i1 0, i1* %cont

	br label %exitif20

exitif20:

	br label %exitif17

ellabel16:
	; If Statment
	%_26 = load i32, i32* %key_aux
	%_27 = load i32, i32* %v_key
	%_28 = icmp slt i32 %_26, %_27
	br i1 %_28, label %iflabel21, label %ellabel22

iflabel21:
	; If Statment
	%_29 = load i8*, i8** %current_node
	; Tree.GetHas_Right(): 7
	%_30 = bitcast i8* %_29 to i8***
	%_31 = load i8**, i8*** %_30
	%_32 = getelementptr i8*, i8** %_31, i32 7
	%_33 = load i8*, i8** %_32
	%_34 = bitcast i8* %_33 to i1 (i8*)*
	%_35 = call i1 %_34(i8* %_29)
	br i1 %_35, label %iflabel24, label %ellabel25

iflabel24:
	%_36 = load i8*, i8** %current_node
	store i8* %_36, i8** %parent_node

	%_37 = load i8*, i8** %current_node
	; Tree.GetRight(): 3
	%_38 = bitcast i8* %_37 to i8***
	%_39 = load i8**, i8*** %_38
	%_40 = getelementptr i8*, i8** %_39, i32 3
	%_41 = load i8*, i8** %_40
	%_42 = bitcast i8* %_41 to i8* (i8*)*
	%_43 = call i8* %_42(i8* %_37)
	store i8* %_43, i8** %current_node

	br label %exitif26

ellabel25:
	store i1 0, i1* %cont

	br label %exitif26

exitif26:

	br label %exitif23

ellabel22:
	; If Statment
	%_44 = load i1, i1* %is_root
	br i1 %_44, label %iflabel27, label %ellabel28

iflabel27:
	; If Statment
	%_45 = load i8*, i8** %current_node
	; Tree.GetHas_Right(): 7
	%_46 = bitcast i8* %_45 to i8***
	%_47 = load i8**, i8*** %_46
	%_48 = getelementptr i8*, i8** %_47, i32 7
	%_49 = load i8*, i8** %_48
	%_50 = bitcast i8* %_49 to i1 (i8*)*
	%_51 = call i1 %_50(i8* %_45)
	%_52 = xor i1 1, %_51
	br label %andStart30

andStart30:
	br i1 %_52, label %truelabel31, label %exitlabel33

truelabel31:
	%_53 = load i8*, i8** %current_node
	; Tree.GetHas_Left(): 8
	%_54 = bitcast i8* %_53 to i8***
	%_55 = load i8**, i8*** %_54
	%_56 = getelementptr i8*, i8** %_55, i32 8
	%_57 = load i8*, i8** %_56
	%_58 = bitcast i8* %_57 to i1 (i8*)*
	%_59 = call i1 %_58(i8* %_53)
	%_60 = xor i1 1, %_59
	br label %rightexp32

rightexp32:
	br label %exitlabel33

exitlabel33:
	%_61 = phi i1 [0, %andStart30], [%_60, %rightexp32]

	br i1 %_61, label %iflabel34, label %ellabel35

iflabel34:
	store i1 1, i1* %ntb

	br label %exitif36

ellabel35:
	; Tree.Remove(): 14
	%_62 = bitcast i8* %this to i8***
	%_63 = load i8**, i8*** %_62
	%_64 = getelementptr i8*, i8** %_63, i32 14
	%_65 = load i8*, i8** %_64
	%_66 = bitcast i8* %_65 to i1 (i8*, i8*, i8*)*
	%_67 = load i8*, i8** %parent_node
	%_68 = load i8*, i8** %current_node
	%_69 = call i1 %_66(i8* %this,i8* %_67,i8* %_68)
	store i1 %_69, i1* %ntb

	br label %exitif36

exitif36:

	br label %exitif29

ellabel28:
	; Tree.Remove(): 14
	%_70 = bitcast i8* %this to i8***
	%_71 = load i8**, i8*** %_70
	%_72 = getelementptr i8*, i8** %_71, i32 14
	%_73 = load i8*, i8** %_72
	%_74 = bitcast i8* %_73 to i1 (i8*, i8*, i8*)*
	%_75 = load i8*, i8** %parent_node
	%_76 = load i8*, i8** %current_node
	%_77 = call i1 %_74(i8* %this,i8* %_75,i8* %_76)
	store i1 %_77, i1* %ntb

	br label %exitif29

exitif29:

	store i1 1, i1* %found

	store i1 0, i1* %cont

	br label %exitif23

exitif23:

	br label %exitif17

exitif17:

	store i1 0, i1* %is_root

	br label %loop3
exitwhile5:
	%_78 = load i1, i1* %found

	ret i1 %_78

}

define i1 @Tree.Remove(i8* %this, i8* %.p_node, i8* %.c_node) {

	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node

	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node

	%ntb = alloca i1
	%auxkey1 = alloca i32
	%auxkey2 = alloca i32

	; If Statment
	%_0 = load i8*, i8** %c_node
	; Tree.GetHas_Left(): 8
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 8
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i1 (i8*)*
	%_6 = call i1 %_5(i8* %_0)
	br i1 %_6, label %iflabel37, label %ellabel38

iflabel37:
	; Tree.RemoveLeft(): 16
	%_7 = bitcast i8* %this to i8***
	%_8 = load i8**, i8*** %_7
	%_9 = getelementptr i8*, i8** %_8, i32 16
	%_10 = load i8*, i8** %_9
	%_11 = bitcast i8* %_10 to i1 (i8*, i8*, i8*)*
	%_12 = load i8*, i8** %p_node
	%_13 = load i8*, i8** %c_node
	%_14 = call i1 %_11(i8* %this,i8* %_12,i8* %_13)
	store i1 %_14, i1* %ntb

	br label %exitif39

ellabel38:
	; If Statment
	%_15 = load i8*, i8** %c_node
	; Tree.GetHas_Right(): 7
	%_16 = bitcast i8* %_15 to i8***
	%_17 = load i8**, i8*** %_16
	%_18 = getelementptr i8*, i8** %_17, i32 7
	%_19 = load i8*, i8** %_18
	%_20 = bitcast i8* %_19 to i1 (i8*)*
	%_21 = call i1 %_20(i8* %_15)
	br i1 %_21, label %iflabel40, label %ellabel41

iflabel40:
	; Tree.RemoveRight(): 15
	%_22 = bitcast i8* %this to i8***
	%_23 = load i8**, i8*** %_22
	%_24 = getelementptr i8*, i8** %_23, i32 15
	%_25 = load i8*, i8** %_24
	%_26 = bitcast i8* %_25 to i1 (i8*, i8*, i8*)*
	%_27 = load i8*, i8** %p_node
	%_28 = load i8*, i8** %c_node
	%_29 = call i1 %_26(i8* %this,i8* %_27,i8* %_28)
	store i1 %_29, i1* %ntb

	br label %exitif42

ellabel41:
	%_30 = load i8*, i8** %c_node
	; Tree.GetKey(): 5
	%_31 = bitcast i8* %_30 to i8***
	%_32 = load i8**, i8*** %_31
	%_33 = getelementptr i8*, i8** %_32, i32 5
	%_34 = load i8*, i8** %_33
	%_35 = bitcast i8* %_34 to i32 (i8*)*
	%_36 = call i32 %_35(i8* %_30)
	store i32 %_36, i32* %auxkey1

	%_37 = load i8*, i8** %p_node
	; Tree.GetLeft(): 4
	%_38 = bitcast i8* %_37 to i8***
	%_39 = load i8**, i8*** %_38
	%_40 = getelementptr i8*, i8** %_39, i32 4
	%_41 = load i8*, i8** %_40
	%_42 = bitcast i8* %_41 to i8* (i8*)*
	%_43 = call i8* %_42(i8* %_37)
	; Tree.GetKey(): 5
	%_44 = bitcast i8* %_43 to i8***
	%_45 = load i8**, i8*** %_44
	%_46 = getelementptr i8*, i8** %_45, i32 5
	%_47 = load i8*, i8** %_46
	%_48 = bitcast i8* %_47 to i32 (i8*)*
	%_49 = call i32 %_48(i8* %_43)
	store i32 %_49, i32* %auxkey2

	; If Statment
	; Tree.Compare(): 11
	%_50 = bitcast i8* %this to i8***
	%_51 = load i8**, i8*** %_50
	%_52 = getelementptr i8*, i8** %_51, i32 11
	%_53 = load i8*, i8** %_52
	%_54 = bitcast i8* %_53 to i1 (i8*, i32, i32)*
	%_55 = load i32, i32* %auxkey1
	%_56 = load i32, i32* %auxkey2
	%_57 = call i1 %_54(i8* %this,i32 %_55,i32 %_56)
	br i1 %_57, label %iflabel43, label %ellabel44

iflabel43:
	%_58 = load i8*, i8** %p_node
	; Tree.SetLeft(): 2
	%_59 = bitcast i8* %_58 to i8***
	%_60 = load i8**, i8*** %_59
	%_61 = getelementptr i8*, i8** %_60, i32 2
	%_62 = load i8*, i8** %_61
	%_63 = bitcast i8* %_62 to i1 (i8*, i8*)*
	%_64 = getelementptr i8, i8* %this, i32 30
	%_65 = bitcast i8* %_64 to i8**
	%_66 = load i8*, i8** %_65
	%_67 = call i1 %_63(i8* %_58,i8* %_66)
	store i1 %_67, i1* %ntb

	%_68 = load i8*, i8** %p_node
	; Tree.SetHas_Left(): 9
	%_69 = bitcast i8* %_68 to i8***
	%_70 = load i8**, i8*** %_69
	%_71 = getelementptr i8*, i8** %_70, i32 9
	%_72 = load i8*, i8** %_71
	%_73 = bitcast i8* %_72 to i1 (i8*, i1)*
	%_74 = call i1 %_73(i8* %_68,i1 0)
	store i1 %_74, i1* %ntb

	br label %exitif45

ellabel44:
	%_75 = load i8*, i8** %p_node
	; Tree.SetRight(): 1
	%_76 = bitcast i8* %_75 to i8***
	%_77 = load i8**, i8*** %_76
	%_78 = getelementptr i8*, i8** %_77, i32 1
	%_79 = load i8*, i8** %_78
	%_80 = bitcast i8* %_79 to i1 (i8*, i8*)*
	%_81 = getelementptr i8, i8* %this, i32 30
	%_82 = bitcast i8* %_81 to i8**
	%_83 = load i8*, i8** %_82
	%_84 = call i1 %_80(i8* %_75,i8* %_83)
	store i1 %_84, i1* %ntb

	%_85 = load i8*, i8** %p_node
	; Tree.SetHas_Right(): 10
	%_86 = bitcast i8* %_85 to i8***
	%_87 = load i8**, i8*** %_86
	%_88 = getelementptr i8*, i8** %_87, i32 10
	%_89 = load i8*, i8** %_88
	%_90 = bitcast i8* %_89 to i1 (i8*, i1)*
	%_91 = call i1 %_90(i8* %_85,i1 0)
	store i1 %_91, i1* %ntb

	br label %exitif45

exitif45:

	br label %exitif42

exitif42:

	br label %exitif39

exitif39:


	ret i1 1

}

define i1 @Tree.RemoveRight(i8* %this, i8* %.p_node, i8* %.c_node) {

	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node

	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node

	%ntb = alloca i1

	; While Statment
	br label %loop6
loop6:
	%_0 = load i8*, i8** %c_node
	; Tree.GetHas_Right(): 7
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 7
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i1 (i8*)*
	%_6 = call i1 %_5(i8* %_0)
	br i1 %_6, label %statments7, label %exitwhile8
statments7:
	%_7 = load i8*, i8** %c_node
	%_8 = load i8*, i8** %c_node
	; Tree.GetRight(): 3
	%_9 = bitcast i8* %_8 to i8***
	%_10 = load i8**, i8*** %_9
	%_11 = getelementptr i8*, i8** %_10, i32 3
	%_12 = load i8*, i8** %_11
	%_13 = bitcast i8* %_12 to i8* (i8*)*
	%_14 = call i8* %_13(i8* %_8)
	; Tree.GetKey(): 5
	%_15 = bitcast i8* %_14 to i8***
	%_16 = load i8**, i8*** %_15
	%_17 = getelementptr i8*, i8** %_16, i32 5
	%_18 = load i8*, i8** %_17
	%_19 = bitcast i8* %_18 to i32 (i8*)*
	%_20 = call i32 %_19(i8* %_14)
	; Tree.SetKey(): 6
	%_21 = bitcast i8* %_7 to i8***
	%_22 = load i8**, i8*** %_21
	%_23 = getelementptr i8*, i8** %_22, i32 6
	%_24 = load i8*, i8** %_23
	%_25 = bitcast i8* %_24 to i1 (i8*, i32)*
	%_26 = call i1 %_25(i8* %_7,i32 %_20)
	store i1 %_26, i1* %ntb

	%_27 = load i8*, i8** %c_node
	store i8* %_27, i8** %p_node

	%_28 = load i8*, i8** %c_node
	; Tree.GetRight(): 3
	%_29 = bitcast i8* %_28 to i8***
	%_30 = load i8**, i8*** %_29
	%_31 = getelementptr i8*, i8** %_30, i32 3
	%_32 = load i8*, i8** %_31
	%_33 = bitcast i8* %_32 to i8* (i8*)*
	%_34 = call i8* %_33(i8* %_28)
	store i8* %_34, i8** %c_node

	br label %loop6
exitwhile8:
	%_35 = load i8*, i8** %p_node
	; Tree.SetRight(): 1
	%_36 = bitcast i8* %_35 to i8***
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 1
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i1 (i8*, i8*)*
	%_41 = getelementptr i8, i8* %this, i32 30
	%_42 = bitcast i8* %_41 to i8**
	%_43 = load i8*, i8** %_42
	%_44 = call i1 %_40(i8* %_35,i8* %_43)
	store i1 %_44, i1* %ntb

	%_45 = load i8*, i8** %p_node
	; Tree.SetHas_Right(): 10
	%_46 = bitcast i8* %_45 to i8***
	%_47 = load i8**, i8*** %_46
	%_48 = getelementptr i8*, i8** %_47, i32 10
	%_49 = load i8*, i8** %_48
	%_50 = bitcast i8* %_49 to i1 (i8*, i1)*
	%_51 = call i1 %_50(i8* %_45,i1 0)
	store i1 %_51, i1* %ntb


	ret i1 1

}

define i1 @Tree.RemoveLeft(i8* %this, i8* %.p_node, i8* %.c_node) {

	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node

	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node

	%ntb = alloca i1

	; While Statment
	br label %loop9
loop9:
	%_0 = load i8*, i8** %c_node
	; Tree.GetHas_Left(): 8
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 8
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i1 (i8*)*
	%_6 = call i1 %_5(i8* %_0)
	br i1 %_6, label %statments10, label %exitwhile11
statments10:
	%_7 = load i8*, i8** %c_node
	%_8 = load i8*, i8** %c_node
	; Tree.GetLeft(): 4
	%_9 = bitcast i8* %_8 to i8***
	%_10 = load i8**, i8*** %_9
	%_11 = getelementptr i8*, i8** %_10, i32 4
	%_12 = load i8*, i8** %_11
	%_13 = bitcast i8* %_12 to i8* (i8*)*
	%_14 = call i8* %_13(i8* %_8)
	; Tree.GetKey(): 5
	%_15 = bitcast i8* %_14 to i8***
	%_16 = load i8**, i8*** %_15
	%_17 = getelementptr i8*, i8** %_16, i32 5
	%_18 = load i8*, i8** %_17
	%_19 = bitcast i8* %_18 to i32 (i8*)*
	%_20 = call i32 %_19(i8* %_14)
	; Tree.SetKey(): 6
	%_21 = bitcast i8* %_7 to i8***
	%_22 = load i8**, i8*** %_21
	%_23 = getelementptr i8*, i8** %_22, i32 6
	%_24 = load i8*, i8** %_23
	%_25 = bitcast i8* %_24 to i1 (i8*, i32)*
	%_26 = call i1 %_25(i8* %_7,i32 %_20)
	store i1 %_26, i1* %ntb

	%_27 = load i8*, i8** %c_node
	store i8* %_27, i8** %p_node

	%_28 = load i8*, i8** %c_node
	; Tree.GetLeft(): 4
	%_29 = bitcast i8* %_28 to i8***
	%_30 = load i8**, i8*** %_29
	%_31 = getelementptr i8*, i8** %_30, i32 4
	%_32 = load i8*, i8** %_31
	%_33 = bitcast i8* %_32 to i8* (i8*)*
	%_34 = call i8* %_33(i8* %_28)
	store i8* %_34, i8** %c_node

	br label %loop9
exitwhile11:
	%_35 = load i8*, i8** %p_node
	; Tree.SetLeft(): 2
	%_36 = bitcast i8* %_35 to i8***
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 2
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i1 (i8*, i8*)*
	%_41 = getelementptr i8, i8* %this, i32 30
	%_42 = bitcast i8* %_41 to i8**
	%_43 = load i8*, i8** %_42
	%_44 = call i1 %_40(i8* %_35,i8* %_43)
	store i1 %_44, i1* %ntb

	%_45 = load i8*, i8** %p_node
	; Tree.SetHas_Left(): 9
	%_46 = bitcast i8* %_45 to i8***
	%_47 = load i8**, i8*** %_46
	%_48 = getelementptr i8*, i8** %_47, i32 9
	%_49 = load i8*, i8** %_48
	%_50 = bitcast i8* %_49 to i1 (i8*, i1)*
	%_51 = call i1 %_50(i8* %_45,i1 0)
	store i1 %_51, i1* %ntb


	ret i1 1

}

define i32 @Tree.Search(i8* %this, i32 %.v_key) {

	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key

	%cont = alloca i1
	%ifound = alloca i32
	%current_node = alloca i8*
	%key_aux = alloca i32

	store i8* %this, i8** %current_node

	store i1 1, i1* %cont

	store i32 0, i32* %ifound

	; While Statment
	br label %loop12
loop12:
	%_0 = load i1, i1* %cont
	br i1 %_0, label %statments13, label %exitwhile14
statments13:
	%_1 = load i8*, i8** %current_node
	; Tree.GetKey(): 5
	%_2 = bitcast i8* %_1 to i8***
	%_3 = load i8**, i8*** %_2
	%_4 = getelementptr i8*, i8** %_3, i32 5
	%_5 = load i8*, i8** %_4
	%_6 = bitcast i8* %_5 to i32 (i8*)*
	%_7 = call i32 %_6(i8* %_1)
	store i32 %_7, i32* %key_aux

	; If Statment
	%_8 = load i32, i32* %v_key
	%_9 = load i32, i32* %key_aux
	%_10 = icmp slt i32 %_8, %_9
	br i1 %_10, label %iflabel46, label %ellabel47

iflabel46:
	; If Statment
	%_11 = load i8*, i8** %current_node
	; Tree.GetHas_Left(): 8
	%_12 = bitcast i8* %_11 to i8***
	%_13 = load i8**, i8*** %_12
	%_14 = getelementptr i8*, i8** %_13, i32 8
	%_15 = load i8*, i8** %_14
	%_16 = bitcast i8* %_15 to i1 (i8*)*
	%_17 = call i1 %_16(i8* %_11)
	br i1 %_17, label %iflabel49, label %ellabel50

iflabel49:
	%_18 = load i8*, i8** %current_node
	; Tree.GetLeft(): 4
	%_19 = bitcast i8* %_18 to i8***
	%_20 = load i8**, i8*** %_19
	%_21 = getelementptr i8*, i8** %_20, i32 4
	%_22 = load i8*, i8** %_21
	%_23 = bitcast i8* %_22 to i8* (i8*)*
	%_24 = call i8* %_23(i8* %_18)
	store i8* %_24, i8** %current_node

	br label %exitif51

ellabel50:
	store i1 0, i1* %cont

	br label %exitif51

exitif51:

	br label %exitif48

ellabel47:
	; If Statment
	%_25 = load i32, i32* %key_aux
	%_26 = load i32, i32* %v_key
	%_27 = icmp slt i32 %_25, %_26
	br i1 %_27, label %iflabel52, label %ellabel53

iflabel52:
	; If Statment
	%_28 = load i8*, i8** %current_node
	; Tree.GetHas_Right(): 7
	%_29 = bitcast i8* %_28 to i8***
	%_30 = load i8**, i8*** %_29
	%_31 = getelementptr i8*, i8** %_30, i32 7
	%_32 = load i8*, i8** %_31
	%_33 = bitcast i8* %_32 to i1 (i8*)*
	%_34 = call i1 %_33(i8* %_28)
	br i1 %_34, label %iflabel55, label %ellabel56

iflabel55:
	%_35 = load i8*, i8** %current_node
	; Tree.GetRight(): 3
	%_36 = bitcast i8* %_35 to i8***
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 3
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i8* (i8*)*
	%_41 = call i8* %_40(i8* %_35)
	store i8* %_41, i8** %current_node

	br label %exitif57

ellabel56:
	store i1 0, i1* %cont

	br label %exitif57

exitif57:

	br label %exitif54

ellabel53:
	store i32 1, i32* %ifound

	store i1 0, i1* %cont

	br label %exitif54

exitif54:

	br label %exitif48

exitif48:

	br label %loop12
exitwhile14:
	%_42 = load i32, i32* %ifound

	ret i32 %_42

}

define i1 @Tree.Print(i8* %this) {

	%current_node = alloca i8*
	%ntb = alloca i1

	store i8* %this, i8** %current_node

	; Tree.RecPrint(): 19
	%_0 = bitcast i8* %this to i8***
	%_1 = load i8**, i8*** %_0
	%_2 = getelementptr i8*, i8** %_1, i32 19
	%_3 = load i8*, i8** %_2
	%_4 = bitcast i8* %_3 to i1 (i8*, i8*)*
	%_5 = load i8*, i8** %current_node
	%_6 = call i1 %_4(i8* %this,i8* %_5)
	store i1 %_6, i1* %ntb


	ret i1 1

}

define i1 @Tree.RecPrint(i8* %this, i8* %.node) {

	%node = alloca i8*
	store i8* %.node, i8** %node

	%ntb = alloca i1

	; If Statment
	%_0 = load i8*, i8** %node
	; Tree.GetHas_Left(): 8
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 8
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i1 (i8*)*
	%_6 = call i1 %_5(i8* %_0)
	br i1 %_6, label %iflabel58, label %ellabel59

iflabel58:
	%_7 = load i8*, i8** %node
	; Tree.GetLeft(): 4
	%_8 = bitcast i8* %_7 to i8***
	%_9 = load i8**, i8*** %_8
	%_10 = getelementptr i8*, i8** %_9, i32 4
	%_11 = load i8*, i8** %_10
	%_12 = bitcast i8* %_11 to i8* (i8*)*
	%_13 = call i8* %_12(i8* %_7)
	; Tree.RecPrint(): 19
	%_14 = bitcast i8* %this to i8***
	%_15 = load i8**, i8*** %_14
	%_16 = getelementptr i8*, i8** %_15, i32 19
	%_17 = load i8*, i8** %_16
	%_18 = bitcast i8* %_17 to i1 (i8*, i8*)*
	%_19 = call i1 %_18(i8* %this,i8* %_13)
	store i1 %_19, i1* %ntb

	br label %exitif60

ellabel59:
	store i1 1, i1* %ntb

	br label %exitif60

exitif60:

	%_20 = load i8*, i8** %node
	; Tree.GetKey(): 5
	%_21 = bitcast i8* %_20 to i8***
	%_22 = load i8**, i8*** %_21
	%_23 = getelementptr i8*, i8** %_22, i32 5
	%_24 = load i8*, i8** %_23
	%_25 = bitcast i8* %_24 to i32 (i8*)*
	%_26 = call i32 %_25(i8* %_20)

	call void (i32) @print_int(i32 %_26)
	; If Statment
	%_27 = load i8*, i8** %node
	; Tree.GetHas_Right(): 7
	%_28 = bitcast i8* %_27 to i8***
	%_29 = load i8**, i8*** %_28
	%_30 = getelementptr i8*, i8** %_29, i32 7
	%_31 = load i8*, i8** %_30
	%_32 = bitcast i8* %_31 to i1 (i8*)*
	%_33 = call i1 %_32(i8* %_27)
	br i1 %_33, label %iflabel61, label %ellabel62

iflabel61:
	%_34 = load i8*, i8** %node
	; Tree.GetRight(): 3
	%_35 = bitcast i8* %_34 to i8***
	%_36 = load i8**, i8*** %_35
	%_37 = getelementptr i8*, i8** %_36, i32 3
	%_38 = load i8*, i8** %_37
	%_39 = bitcast i8* %_38 to i8* (i8*)*
	%_40 = call i8* %_39(i8* %_34)
	; Tree.RecPrint(): 19
	%_41 = bitcast i8* %this to i8***
	%_42 = load i8**, i8*** %_41
	%_43 = getelementptr i8*, i8** %_42, i32 19
	%_44 = load i8*, i8** %_43
	%_45 = bitcast i8* %_44 to i1 (i8*, i8*)*
	%_46 = call i1 %_45(i8* %this,i8* %_40)
	store i1 %_46, i1* %ntb

	br label %exitif63

ellabel62:
	store i1 1, i1* %ntb

	br label %exitif63

exitif63:


	ret i1 1

}

