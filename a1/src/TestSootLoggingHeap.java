import java.io.File;
import java.util.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.dava.internal.javaRep.DIntConstant;

public class TestSootLoggingHeap extends BodyTransformer {

	private static SootMethodRef logFieldAccMethod;

	public static void main(String[] args)	{

		String mainclass = "HelloThread";

		//output Jimple
		//Options.v().set_output_format(1);

//		//set classpath
	    String javapath = System.getProperty("java.class.path");
	    String jredir = System.getProperty("java.home")+"/lib/rt.jar";
	    String path = javapath+File.pathSeparator+jredir;
	    Scene.v().setSootClassPath(path);

        //add an intra-procedural analysis phase to Soot
	    TestSootLoggingHeap analysis = new TestSootLoggingHeap();
	    PackManager.v().getPack("jtp").add(new Transform("jtp.TestSootLoggingHeap", analysis));

        //load and set main class
	    Options.v().set_app(true);
	    SootClass appclass = Scene.v().loadClassAndSupport(mainclass);
	    Scene.v().setMainClass(appclass);
		SootClass logClass = Scene.v().loadClassAndSupport("Log");
		//logFieldAccMethod = logClass.getMethod("void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)").makeRef();
		logFieldAccMethod = logClass.getMethod("void logFieldAcc(java.lang.String,java.lang.String,boolean,boolean)").makeRef();
		Scene.v().loadNecessaryClasses();

        //start working
	    PackManager.v().runPacks();

	    PackManager.v().writeOutput();
	}

	@Override
	protected void internalTransform(Body b, String phaseName,
		Map<String, String> options) {
		
		//we don't instrument Log class
		if(!b.getMethod().getDeclaringClass().getName().equals("Log"))
		{
			Iterator<Unit> it = b.getUnits().snapshotIterator();
		    while(it.hasNext()){
		    	Stmt stmt = (Stmt)it.next();
		    	if (stmt.containsFieldRef()) {
		    		//your code starts here
		    		
		    		String name = stmt.getFieldRef().getField().toString();
		    		boolean Static = stmt.getFieldRef().getField().isStatic();
		    		int isStatic = 0;
		    		int isWrite = 0;
		    		if(Static)
		    		{
		    			isStatic = 1;
		    		}
		    		else
		    		{
		    			isStatic = 0;
		    		}
		    		AssignStmt temp = (AssignStmt)stmt;
	    			Value temp2 = temp.getLeftOp();
		    		//String o = stmt.getFieldRef().getClass().getName();
		    		boolean Write = false;
	    			ArrayList<Value> arr = new ArrayList<Value>();
	    			
	    			if(temp2.equivHashCode() > 0 && !name.contains("Print"))
	    			{
	    				Write = true;
	    			}	    	
	    			
	    			if(Write)
	    			{
	    				isWrite = 1;
	    			}
	    			else
	    			{
	    				isWrite = 0;
	    			}
	    			
	    			arr.add(StringConstant.v("[Thread-0,5,main]"));
	    			arr.add(StringConstant.v(name));
	    			arr.add(DIntConstant.v(isStatic, BooleanType.v()));
	    			arr.add(DIntConstant.v(isWrite, BooleanType.v()));
	    			//arr.add();
	    			//args.add(IntConstant.v(isWrite));
	    			
	    			//System.out.println(stmt.getFieldRef().);
	    			//Log.logFieldAcc(name, o);
	    			//rgs.add(Boolean.valueOf(isStatic));
	    			
	    			InvokeExpr printExpr = Jimple.v().newStaticInvokeExpr(logFieldAccMethod, arr);
	    			InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(printExpr);
	    			b.getUnits().insertBefore(invokeStmt, stmt);
	    			
	    			//Log.logFieldAcc(o, name, isStatic, isWrite);
	    			//System.out.println(name);
		    		//System.out.println(isWrite);
		    		//System.out.println("");
		    		
		    		
		    	}
		    }
		}
	}
}