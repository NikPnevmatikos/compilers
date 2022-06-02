@.BBS_vtable = global [4 x i8*] [
	i8* bitcast (i32 (i8*, i32)* @BBS.Start to i8*),
	i8* bitcast (i32 (i8*)* @BBS.Sort to i8*),
	i8* bitcast (i32 (i8*)* @BBS.Print to i8*),
	i8* bitcast (i32 (i8*, i32)* @BBS.Init to i8*)
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


	; Allocating object type BBS
	%_0 = call i8* @calloc(i32 1, i32 20)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [4 x i8*], [4 x i8*]* @.BBS_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	; BBS.Start(): 0
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*, i32)*
	%_8 = call i32 %_7(i8* %_0,i32 10)

	call void (i32) @print_int(i32 %_8)

	ret i32 0
}

define i32 @BBS.Start(i8* %this, i32 %.sz) {

	%sz = alloca i32
	store i32 %.sz, i32* %sz

	%aux01 = alloca i32

	; BBS.Init(): 3
	%_0 = bitcast i8* %this to i8***
	%_1 = load i8**, i8*** %_0
	%_2 = getelementptr i8*, i8** %_1, i32 3
	%_3 = load i8*, i8** %_2
	%_4 = bitcast i8* %_3 to i32 (i8*, i32)*
	%_5 = load i32, i32* %sz
	%_6 = call i32 %_4(i8* %this,i32 %_5)
	store i32 %_6, i32* %aux01

	; BBS.Print(): 2
	%_7 = bitcast i8* %this to i8***
	%_8 = load i8**, i8*** %_7
	%_9 = getelementptr i8*, i8** %_8, i32 2
	%_10 = load i8*, i8** %_9
	%_11 = bitcast i8* %_10 to i32 (i8*)*
	%_12 = call i32 %_11(i8* %this)
	store i32 %_12, i32* %aux01


	call void (i32) @print_int(i32 99999)
	; BBS.Sort(): 1
	%_13 = bitcast i8* %this to i8***
	%_14 = load i8**, i8*** %_13
	%_15 = getelementptr i8*, i8** %_14, i32 1
	%_16 = load i8*, i8** %_15
	%_17 = bitcast i8* %_16 to i32 (i8*)*
	%_18 = call i32 %_17(i8* %this)
	store i32 %_18, i32* %aux01

	; BBS.Print(): 2
	%_19 = bitcast i8* %this to i8***
	%_20 = load i8**, i8*** %_19
	%_21 = getelementptr i8*, i8** %_20, i32 2
	%_22 = load i8*, i8** %_21
	%_23 = bitcast i8* %_22 to i32 (i8*)*
	%_24 = call i32 %_23(i8* %this)
	store i32 %_24, i32* %aux01


	ret i32 0

}

define i32 @BBS.Sort(i8* %this) {

	%nt = alloca i32
	%i = alloca i32
	%aux02 = alloca i32
	%aux04 = alloca i32
	%aux05 = alloca i32
	%aux06 = alloca i32
	%aux07 = alloca i32
	%j = alloca i32
	%t = alloca i32

	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	%_3 = sub i32 %_2, 1
	store i32 %_3, i32* %i

	%_4 = sub i32 0, 1
	store i32 %_4, i32* %aux02

	; While Statment
	br label %loop0
loop0:
	%_5 = load i32, i32* %aux02
	%_6 = load i32, i32* %i
	%_7 = icmp slt i32 %_5, %_6
	br i1 %_7, label %statments1, label %exitwhile2
statments1:
	store i32 1, i32* %j

	; While Statment
	br label %loop3
loop3:
	%_8 = load i32, i32* %i
	%_9 = add i32 %_8, 1
	%_10 = load i32, i32* %j
	%_11 = icmp slt i32 %_10, %_9
	br i1 %_11, label %statments4, label %exitwhile5
statments4:
	%_12 = load i32, i32* %j
	%_13 = sub i32 %_12, 1
	store i32 %_13, i32* %aux07

	; Array Lookup
	%_14 = getelementptr i8, i8* %this, i32 8
	%_15 = bitcast i8* %_14 to i32**
	%_16 = load i32*, i32** %_15
	%_17 = load i32, i32* %aux07
	%_18 = load i32, i32* %_16
	%_19 = icmp ult i32 %_17, %_18
	br i1 %_19, label %oob0, label %oob1

oob0:
	%_20 = add i32 %_17, 1
	%_21 = getelementptr i32, i32* %_16, i32 %_20
	%_22 = load i32, i32* %_21
	br label %oob2

oob1:
	call void @throw_oob()
	br label %oob2

oob2:
	store i32 %_22, i32* %aux04

	; Array Lookup
	%_23 = getelementptr i8, i8* %this, i32 8
	%_24 = bitcast i8* %_23 to i32**
	%_25 = load i32*, i32** %_24
	%_26 = load i32, i32* %j
	%_27 = load i32, i32* %_25
	%_28 = icmp ult i32 %_26, %_27
	br i1 %_28, label %oob3, label %oob4

oob3:
	%_29 = add i32 %_26, 1
	%_30 = getelementptr i32, i32* %_25, i32 %_29
	%_31 = load i32, i32* %_30
	br label %oob5

oob4:
	call void @throw_oob()
	br label %oob5

oob5:
	store i32 %_31, i32* %aux05

	; If Statment
	%_32 = load i32, i32* %aux05
	%_33 = load i32, i32* %aux04
	%_34 = icmp slt i32 %_32, %_33
	br i1 %_34, label %iflabel6, label %ellabel7

iflabel6:
	%_35 = load i32, i32* %j
	%_36 = sub i32 %_35, 1
	store i32 %_36, i32* %aux06

	; Array Lookup
	%_37 = getelementptr i8, i8* %this, i32 8
	%_38 = bitcast i8* %_37 to i32**
	%_39 = load i32*, i32** %_38
	%_40 = load i32, i32* %aux06
	%_41 = load i32, i32* %_39
	%_42 = icmp ult i32 %_40, %_41
	br i1 %_42, label %oob9, label %oob10

oob9:
	%_43 = add i32 %_40, 1
	%_44 = getelementptr i32, i32* %_39, i32 %_43
	%_45 = load i32, i32* %_44
	br label %oob11

oob10:
	call void @throw_oob()
	br label %oob11

oob11:
	store i32 %_45, i32* %t

	; Array Assignment
	%_46 = getelementptr i8, i8* %this, i32 8
	%_47 = bitcast i8* %_46 to i32**
	%_48 = load i32*, i32** %_47
	%_49 = load i32, i32* %aux06
	%_50 = load i32, i32* %_48
	%_51 = icmp ult i32 %_49, %_50
	br i1 %_51, label %oob12, label %oob13

oob12:
	%_52 = add i32 %_49, 1
	%_53 = getelementptr i32, i32* %_48, i32 %_52
	; Array Lookup
	%_54 = getelementptr i8, i8* %this, i32 8
	%_55 = bitcast i8* %_54 to i32**
	%_56 = load i32*, i32** %_55
	%_57 = load i32, i32* %j
	%_58 = load i32, i32* %_56
	%_59 = icmp ult i32 %_57, %_58
	br i1 %_59, label %oob15, label %oob16

oob15:
	%_60 = add i32 %_57, 1
	%_61 = getelementptr i32, i32* %_56, i32 %_60
	%_62 = load i32, i32* %_61
	br label %oob17

oob16:
	call void @throw_oob()
	br label %oob17

oob17:
	store i32 %_62, i32* %_53
	br label %oob14

oob13:
	call void @throw_oob()
	br label %oob14

oob14:
	; Array Assignment
	%_63 = getelementptr i8, i8* %this, i32 8
	%_64 = bitcast i8* %_63 to i32**
	%_65 = load i32*, i32** %_64
	%_66 = load i32, i32* %j
	%_67 = load i32, i32* %_65
	%_68 = icmp ult i32 %_66, %_67
	br i1 %_68, label %oob18, label %oob19

oob18:
	%_69 = add i32 %_66, 1
	%_70 = getelementptr i32, i32* %_65, i32 %_69
	%_71 = load i32, i32* %t
	store i32 %_71, i32* %_70
	br label %oob20

oob19:
	call void @throw_oob()
	br label %oob20

oob20:
	br label %exitif8

ellabel7:
	store i32 0, i32* %nt

	br label %exitif8

exitif8:

	%_72 = load i32, i32* %j
	%_73 = add i32 %_72, 1
	store i32 %_73, i32* %j

	br label %loop3
exitwhile5:
	%_74 = load i32, i32* %i
	%_75 = sub i32 %_74, 1
	store i32 %_75, i32* %i

	br label %loop0
exitwhile2:

	ret i32 0

}

define i32 @BBS.Print(i8* %this) {

	%j = alloca i32

	store i32 0, i32* %j

	; While Statment
	br label %loop6
loop6:
	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	%_3 = load i32, i32* %j
	%_4 = icmp slt i32 %_3, %_2
	br i1 %_4, label %statments7, label %exitwhile8
statments7:
	; Array Lookup
	%_5 = getelementptr i8, i8* %this, i32 8
	%_6 = bitcast i8* %_5 to i32**
	%_7 = load i32*, i32** %_6
	%_8 = load i32, i32* %j
	%_9 = load i32, i32* %_7
	%_10 = icmp ult i32 %_8, %_9
	br i1 %_10, label %oob21, label %oob22

oob21:
	%_11 = add i32 %_8, 1
	%_12 = getelementptr i32, i32* %_7, i32 %_11
	%_13 = load i32, i32* %_12
	br label %oob23

oob22:
	call void @throw_oob()
	br label %oob23

oob23:

	call void (i32) @print_int(i32 %_13)
	%_14 = load i32, i32* %j
	%_15 = add i32 %_14, 1
	store i32 %_15, i32* %j

	br label %loop6
exitwhile8:

	ret i32 0

}

define i32 @BBS.Init(i8* %this, i32 %.sz) {

	%sz = alloca i32
	store i32 %.sz, i32* %sz


	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %sz
	store i32 %_2, i32* %_1

	; Int Array Allocation
	%_3 = load i32, i32* %sz
	%_4 = icmp slt i32 %_3, 0
	br i1 %_4, label %arr_alloc24, label %arr_alloc25

arr_alloc24:
	call void @throw_oob()
	br label %arr_alloc25

arr_alloc25:
	%_5 = add i32 %_3, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 %_3, i32* %_7

	%_8 = getelementptr i8, i8* %this, i32 8
	%_9 = bitcast i8* %_8 to i32**
	store i32* %_7, i32** %_9

	; Array Assignment
	%_10 = getelementptr i8, i8* %this, i32 8
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = load i32, i32* %_12
	%_14 = icmp ult i32 0, %_13
	br i1 %_14, label %oob26, label %oob27

oob26:
	%_15 = add i32 0, 1
	%_16 = getelementptr i32, i32* %_12, i32 %_15
	store i32 20, i32* %_16
	br label %oob28

oob27:
	call void @throw_oob()
	br label %oob28

oob28:
	; Array Assignment
	%_17 = getelementptr i8, i8* %this, i32 8
	%_18 = bitcast i8* %_17 to i32**
	%_19 = load i32*, i32** %_18
	%_20 = load i32, i32* %_19
	%_21 = icmp ult i32 1, %_20
	br i1 %_21, label %oob29, label %oob30

oob29:
	%_22 = add i32 1, 1
	%_23 = getelementptr i32, i32* %_19, i32 %_22
	store i32 7, i32* %_23
	br label %oob31

oob30:
	call void @throw_oob()
	br label %oob31

oob31:
	; Array Assignment
	%_24 = getelementptr i8, i8* %this, i32 8
	%_25 = bitcast i8* %_24 to i32**
	%_26 = load i32*, i32** %_25
	%_27 = load i32, i32* %_26
	%_28 = icmp ult i32 2, %_27
	br i1 %_28, label %oob32, label %oob33

oob32:
	%_29 = add i32 2, 1
	%_30 = getelementptr i32, i32* %_26, i32 %_29
	store i32 12, i32* %_30
	br label %oob34

oob33:
	call void @throw_oob()
	br label %oob34

oob34:
	; Array Assignment
	%_31 = getelementptr i8, i8* %this, i32 8
	%_32 = bitcast i8* %_31 to i32**
	%_33 = load i32*, i32** %_32
	%_34 = load i32, i32* %_33
	%_35 = icmp ult i32 3, %_34
	br i1 %_35, label %oob35, label %oob36

oob35:
	%_36 = add i32 3, 1
	%_37 = getelementptr i32, i32* %_33, i32 %_36
	store i32 18, i32* %_37
	br label %oob37

oob36:
	call void @throw_oob()
	br label %oob37

oob37:
	; Array Assignment
	%_38 = getelementptr i8, i8* %this, i32 8
	%_39 = bitcast i8* %_38 to i32**
	%_40 = load i32*, i32** %_39
	%_41 = load i32, i32* %_40
	%_42 = icmp ult i32 4, %_41
	br i1 %_42, label %oob38, label %oob39

oob38:
	%_43 = add i32 4, 1
	%_44 = getelementptr i32, i32* %_40, i32 %_43
	store i32 2, i32* %_44
	br label %oob40

oob39:
	call void @throw_oob()
	br label %oob40

oob40:
	; Array Assignment
	%_45 = getelementptr i8, i8* %this, i32 8
	%_46 = bitcast i8* %_45 to i32**
	%_47 = load i32*, i32** %_46
	%_48 = load i32, i32* %_47
	%_49 = icmp ult i32 5, %_48
	br i1 %_49, label %oob41, label %oob42

oob41:
	%_50 = add i32 5, 1
	%_51 = getelementptr i32, i32* %_47, i32 %_50
	store i32 11, i32* %_51
	br label %oob43

oob42:
	call void @throw_oob()
	br label %oob43

oob43:
	; Array Assignment
	%_52 = getelementptr i8, i8* %this, i32 8
	%_53 = bitcast i8* %_52 to i32**
	%_54 = load i32*, i32** %_53
	%_55 = load i32, i32* %_54
	%_56 = icmp ult i32 6, %_55
	br i1 %_56, label %oob44, label %oob45

oob44:
	%_57 = add i32 6, 1
	%_58 = getelementptr i32, i32* %_54, i32 %_57
	store i32 6, i32* %_58
	br label %oob46

oob45:
	call void @throw_oob()
	br label %oob46

oob46:
	; Array Assignment
	%_59 = getelementptr i8, i8* %this, i32 8
	%_60 = bitcast i8* %_59 to i32**
	%_61 = load i32*, i32** %_60
	%_62 = load i32, i32* %_61
	%_63 = icmp ult i32 7, %_62
	br i1 %_63, label %oob47, label %oob48

oob47:
	%_64 = add i32 7, 1
	%_65 = getelementptr i32, i32* %_61, i32 %_64
	store i32 9, i32* %_65
	br label %oob49

oob48:
	call void @throw_oob()
	br label %oob49

oob49:
	; Array Assignment
	%_66 = getelementptr i8, i8* %this, i32 8
	%_67 = bitcast i8* %_66 to i32**
	%_68 = load i32*, i32** %_67
	%_69 = load i32, i32* %_68
	%_70 = icmp ult i32 8, %_69
	br i1 %_70, label %oob50, label %oob51

oob50:
	%_71 = add i32 8, 1
	%_72 = getelementptr i32, i32* %_68, i32 %_71
	store i32 19, i32* %_72
	br label %oob52

oob51:
	call void @throw_oob()
	br label %oob52

oob52:
	; Array Assignment
	%_73 = getelementptr i8, i8* %this, i32 8
	%_74 = bitcast i8* %_73 to i32**
	%_75 = load i32*, i32** %_74
	%_76 = load i32, i32* %_75
	%_77 = icmp ult i32 9, %_76
	br i1 %_77, label %oob53, label %oob54

oob53:
	%_78 = add i32 9, 1
	%_79 = getelementptr i32, i32* %_75, i32 %_78
	store i32 5, i32* %_79
	br label %oob55

oob54:
	call void @throw_oob()
	br label %oob55

oob55:

	ret i32 0

}

