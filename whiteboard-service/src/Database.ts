import { DataTypes, Sequelize } from "sequelize";
import { Whiteboard } from "./Whiteboard";

const dbhost = process.env["IS_CONTAINER"] ? "postgres:jizzle@whiteboards-database" : "postgres@localhost"

const sequelize = new Sequelize(`postgres://${dbhost}:5432/postgres`, { logging: false })

export async function connectDatabase() {
    try {
        await sequelize.authenticate();
        console.log('Database connected.');
    } catch (error) {
        console.error('Unable to connect to the database:', error);
    }
}

Whiteboard.init(
    {
        conversationId: {
            type: DataTypes.STRING(20),
            primaryKey: true,
        },
        image: {
            type: new DataTypes.STRING(1000 * 1000),
            allowNull: false,
        },
    },
    {
        tableName: "whiteboards",
        sequelize, // passing the `sequelize` instance is required
    }
).sync({ alter: true });
